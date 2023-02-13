package com.increff.pdf.controller;

import java.beans.PropertyEditorSupport;
import java.text.Format;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.pdf.model.data.MessageData;
import com.increff.pdf.util.ApiException;

@RestControllerAdvice
public class AppRestControllerAdvice {
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException e) {
            MessageData data = new MessageData();
            data.setMessage(e.getMessage());
            return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handle(Throwable e) {
            MessageData data = new MessageData();
            data.setMessage("An unknown error has occurred - " + e.getMessage());
            return data;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final MessageData handleConstraintViolation(ConstraintViolationException ex) {
            List<String> details = ex.getConstraintViolations()
                            .parallelStream()
                            .map(e -> e.getMessage())
                            .collect(Collectors.toList());

            MessageData data = new MessageData();

            data.setMessage(details.toString());
            return data;
    }

    private static class Editor<T> extends PropertyEditorSupport {

            private final Function<String, T> parser;
            private final Format format;

            public Editor(Function<String, T> parser, Format format) {

                    this.parser = parser;
                    this.format = format;
            }

            public void setAsText(String text) {

                    setValue(this.parser.apply(text));
            }

            public String getAsText() {

                    return format.format((T) getValue());
            }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
            binder.registerCustomEditor(Optional.class, "optionalZonedDate", new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) {
                            setValue(Optional.ofNullable(text)
                                            .map(s -> ZonedDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME)));
                    }

                    @Override
                    public String getAsText() {
                            return ((Optional<ZonedDateTime>) getValue()).map(ZonedDateTime::toString).orElse(null);
                    }
            });
            binder.registerCustomEditor(
                            Instant.class,
                            new Editor<>(
                                            Instant::parse,
                                            DateTimeFormatter.ISO_INSTANT.toFormat()));

            binder.registerCustomEditor(
                            LocalDate.class,
                            new Editor<>(
                                            text -> LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE),
                                            DateTimeFormatter.ISO_LOCAL_DATE.toFormat()));

            binder.registerCustomEditor(
                            LocalDateTime.class,
                            new Editor<>(
                                            text -> LocalDateTime.parse(text,
                                                            DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                            DateTimeFormatter.ISO_LOCAL_DATE_TIME.toFormat()));

            binder.registerCustomEditor(
                            LocalTime.class,
                            new Editor<>(
                                            text -> LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME),
                                            DateTimeFormatter.ISO_LOCAL_TIME.toFormat()));

            binder.registerCustomEditor(
                            OffsetDateTime.class,
                            new Editor<>(
                                            text -> OffsetDateTime.parse(text,
                                                            DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                                            DateTimeFormatter.ISO_OFFSET_DATE_TIME.toFormat()));

            binder.registerCustomEditor(
                            OffsetTime.class,
                            new Editor<>(
                                            text -> OffsetTime.parse(text, DateTimeFormatter.ISO_OFFSET_TIME),
                                            DateTimeFormatter.ISO_OFFSET_TIME.toFormat()));

            binder.registerCustomEditor(
                            ZonedDateTime.class,
                            new Editor<>(
                                            text -> ZonedDateTime.parse(text,
                                                            DateTimeFormatter.ISO_ZONED_DATE_TIME),
                                            DateTimeFormatter.ISO_ZONED_DATE_TIME.toFormat()));
    }
        
}

