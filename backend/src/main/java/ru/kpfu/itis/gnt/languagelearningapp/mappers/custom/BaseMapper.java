package ru.kpfu.itis.gnt.languagelearningapp.mappers.custom;

public interface BaseMapper <To, From>{
    To mapTo(From model);
    From mapFrom(To model);
}
