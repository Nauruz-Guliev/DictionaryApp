package ru.kpfu.itis.gnt.languagelearningapp.utils;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class LocaleConverter implements GenericConverter {

    private final Class<?> clazz;

    public LocaleConverter() {
        super();
        this.clazz = Locale.class;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> types = new HashSet<>();
        types.add(new ConvertiblePair(String.class, this.clazz));
        types.add(new ConvertiblePair(this.clazz, String.class));
        return types;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (String.class.equals(sourceType.getType())) {
            if (!StringUtils.hasText((String) source) || source.equals("NONE")) {
                return null;
            }
            try {
                return LocaleUtils.toLocale((String) source);
            } catch (Exception ex) {
                return null;
            }
        } else if (this.clazz.equals(sourceType.getType())) {
            if (source == null) {
                return null;
            } else {
                return source.toString();
            }
        }
        throw new IllegalArgumentException("Cannot convert " + source + " " + "into a suitable type");
    }
}
