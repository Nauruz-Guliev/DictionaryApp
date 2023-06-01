package ru.kpfu.itis.gnt.languagelearningapp.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.DictionaryApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.entities.*;
import ru.kpfu.itis.gnt.languagelearningapp.exception.DatabaseException;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.DictionaryMapper;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.DictionaryYandexApiResponseMapper;
import ru.kpfu.itis.gnt.languagelearningapp.dto.DictionaryDto;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;
import ru.kpfu.itis.gnt.languagelearningapp.repository.*;

import java.io.IOException;
import java.util.*;

@Service
public class DictionaryService {
    private final Retrofit dictionaryRetrofit;
    private final DictionaryYandexApiResponseMapper dictionaryMapper;
    private final SynonymRepository synonymRepository;
    private final MeaningRepository meaningRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final DictionaryRepository dictionaryRepository;
    private final LocaleRepository localeRepository;
    private final WordTypeRepository wordTypeRepository;
    private final DictionaryMapper dictionaryLocalMapper;
    private final AudioService audioService;

    public DictionaryService(@Qualifier(QualifierConstants.DICTIONARY_QUALIFIER) Retrofit dictionaryRetrofit,
                             LocaleRepository localeRepository,
                             DictionaryRepository dictionaryRepository,
                             SynonymRepository synonymRepository,
                             MeaningRepository meaningRepository,
                             DictionaryYandexApiResponseMapper dictionaryMapper,
                             ImageService imageService,
                             UserRepository userRepository,
                             WordTypeRepository wordTypeRepository,
                             DictionaryMapper dictionaryLocalMapper,
                             AudioService audioService) {

        this.dictionaryRetrofit = dictionaryRetrofit;
        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryMapper = dictionaryMapper;
        this.localeRepository = localeRepository;
        this.synonymRepository = synonymRepository;
        this.meaningRepository = meaningRepository;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.wordTypeRepository = wordTypeRepository;
        this.dictionaryLocalMapper = dictionaryLocalMapper;
        this.audioService = audioService;
    }

    public DictionaryDto getDictionaryModel(TranslationDto dto, UserDto userDto) {
        DictionaryDto model = new DictionaryDto();
        // получаем перевод. Если не получили - исключение
        model.setDictionary(getDictionaryItems(dto, userDto));
        // получаем аудио
        model.setAudio(audioService.loadAudioRemote(dto, dto.getText()));
        return model;
    }

    private List<DictionaryItem> getDictionaryItems(TranslationDto dto, UserDto userDto) {
        try {
            List<WordEntity> list = dictionaryRepository.findByTextIgnoreCaseAndLocale(dto.getText(), getLocale(dto.getFrom()));
            if (!list.isEmpty()) {
                return getDictionaryItemsLocal(dto, userDto, list);
            } else {
                return getDictionaryItemsRemote(dto, userDto);
            }
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.NOT_FOUND.DICTIONARY + " " + exception.getMessage());
        }
    }

    private List<DictionaryItem> getDictionaryItemsLocal(TranslationDto dto, UserDto userDto, List<WordEntity> list) {
        Set<DictionaryItem> items = new HashSet<>();
        for (WordEntity entity : list) {
            DictionaryItem item = DictionaryItem.builder()
                    .id(entity.getId())
                    //перевод
                    .originalWord(dto.getText())
                    //значения
                    .meaning(entity.getMeanings().stream().map(MeaningEntity::getText).toList())
                    // синонимы
                    .synonyms(entity.getSynonyms().stream().map(SynonymEntity::getText).toList())
                    // избранный
                    .isFavorite(checkIfFavorite(entity, userDto))
                    // картинки
                    .images(entity.getImages().stream().map(ImageUrlEntity::getUrl).toList())
                    .build();
            items.add(item);
        }
        return new ArrayList<>(items);
    }

    private List<DictionaryItem> getDictionaryItemsRemote(TranslationDto dto, UserDto userDto) throws IOException {
        DictionaryApi dictionaryApi = dictionaryRetrofit.create(DictionaryApi.class);
        YandexDictionaryResponse responseTranslation = dictionaryApi.getTranslation(dto.getFrom().getLanguage() + "-" + dto.getTo().getLanguage(), dto.getText()).execute().body();
        //если не нашло, то выбрасываем исключение, которое ниже сразу отлавливаем
        if (responseTranslation == null) throw new RuntimeException();
        // если нашло, то кэшируем
        List<DictionaryItem> translationList = dictionaryMapper.mapTo(responseTranslation);
        for (DictionaryItem item : translationList) {
            //сначала создаем сущность общую

            WordEntity wordEntity = dictionaryRepository.findByTextIgnoreCaseAndType(
                    dto.getText(), item.getType());

            if (wordEntity == null) {
                wordEntity = WordEntity.builder()
                        // text
                        .text(dto.getText())
                        //время сохранения
                        .dateAdded(Calendar.getInstance().getTimeInMillis())
                        // локаль
                        .build();
            }

            //синонимы
            List<SynonymEntity> synonyms = getSynonyms(item);
            wordEntity.setSynonyms(synonyms);

            //значения
            List<MeaningEntity> meanings = getMeanings(item);
            wordEntity.setMeanings(meanings);

            //локаль
            LocaleEntity locale = getLocale(dto.getFrom());
            wordEntity.setLocale(locale);

            //тип слова
            WordTypeEntity wordTypeEntity = getWordType(item.getType());
            wordEntity.setType(wordTypeEntity);

            //ссылки на изображения
            List<ImageUrlEntity> imageList = imageService.getImageUrlEntityList(wordEntity, dto.getText(), dto.getFrom().getLanguage());
            item.setImages(imageList.stream().map(ImageUrlEntity::getUrl).toList());
            wordEntity.setImages(imageList);
            wordEntity = dictionaryRepository.saveAndFlush(wordEntity);
            item.setFavorite(checkIfFavorite(wordEntity, userDto));
            item.setId(wordEntity.getId());
        }
        return translationList;
    }

    private WordTypeEntity getWordType(String text) {
        return wordTypeRepository.findByTextIgnoreCase(text).orElseGet(() -> wordTypeRepository.save(WordTypeEntity.builder().text(text).build()));
    }

    private boolean checkIfFavorite(WordEntity wordEntity, UserDto userDto) {
        return dictionaryRepository.isFavorite(userDto.getId(), wordEntity.getId());
    }


    private LocaleEntity getLocale(Locale locale) {
        return localeRepository.findByTextIgnoreCase(locale.toString()).orElse(
                LocaleEntity.builder()
                        .text(locale.toString())
                        .build()
        );
    }

    private List<SynonymEntity> getSynonyms(DictionaryItem item) {
        try {
            List<SynonymEntity> list = new ArrayList<>();
            for (String meaning : item.getSynonyms()) {
                Optional<SynonymEntity> synonymEntity = synonymRepository.findByTextIgnoreCase(meaning);
                if (synonymEntity.isPresent()) {
                    list.add(synonymEntity.get());
                } else {
                    list.add((SynonymEntity.builder().text(meaning).build()));
                }
            }
            return list;
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    private List<MeaningEntity> getMeanings(DictionaryItem item) {
        try {
            List<MeaningEntity> list = new ArrayList<>();
            for (String meaning : item.getMeaning()) {
                Optional<MeaningEntity> meaningEntity = meaningRepository.findByTextIgnoreCase(meaning);
                if (meaningEntity.isPresent()) {
                    list.add(meaningEntity.get());
                } else {
                    list.add((MeaningEntity.builder().text(meaning).build()));
                }
            }
            return list;
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }


    public DictionaryItem addFavorite(DictionaryItem item, UserDto userDto) {
        try {
            UserEntity userEntity = userRepository.findByLogin(userDto.getLogin()).get();
            WordEntity wordEntity = dictionaryRepository.findByTextIgnoreCaseAndType(item.getOriginalWord(), item.getType());
            userEntity.getWords().add(wordEntity);
            userRepository.saveAndFlush(userEntity);
            return dictionaryLocalMapper.mapTo(dictionaryRepository.findById(item.getId()).get(), true);
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITE_ADD_FAIL + " due to " + exception.getMessage());
        }
    }


    public DictionaryItem deleteFavorite(DictionaryItem item, UserDto userDto) {
        try {
            UserEntity userEntity = userRepository.findByLogin(userDto.getLogin()).get();
            WordEntity wordEntity = dictionaryRepository.findByTextIgnoreCaseAndType(item.getOriginalWord(), item.getType());
            userEntity.getWords().remove(wordEntity);
            userRepository.saveAndFlush(userEntity);
            return dictionaryLocalMapper.mapTo(dictionaryRepository.findById(item.getId()).get(), false);
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITE_REMOVE_FAIL + " due to " + exception.getMessage());
        }
    }

    public List<DictionaryItem> getUserWordEntityList(UserDto userDto) {
        try {
            List<DictionaryItem> list = new ArrayList<>();
            UserEntity userEntity = userRepository.findByLogin(userDto.getLogin()).orElseThrow(RuntimeException::new);
            for (WordEntity wordEntity : userEntity.getWords()) {
                list.add(dictionaryLocalMapper.mapTo(wordEntity, true));
            }
            return list;
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITES_FAIL);
        }
    }


    public Page<DictionaryItem> getUserWordEntityList(Pageable pageable, UserDto userDto) {
        try {
            Page<WordEntity> wordEntities = dictionaryRepository.findByIds(userRepository.getFavoriteWordIds(userDto.getId()), pageable);
            return wordEntities.map(wordEntity -> {
                boolean isFavorite = dictionaryRepository.isFavorite(userDto.getId(), wordEntity.getId());
                return dictionaryLocalMapper.mapTo(wordEntity, isFavorite);
            });
        } catch (Exception ex) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.PAGING_FAILED);
        }
    }

    public String getWord() {
        Optional<String> word = dictionaryRepository.findWordWithMostMeanings();
        return word.orElse("No words yet.");
    }
}
