package com.increff.pos.controller;

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
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.service.ApiException;

@RestControllerAdvice
public class AppRestControllerAdvice {

        
        /** 
         * APIException handler
         * @param apiException
         * @return MessageData
         */
        @ExceptionHandler(ApiException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public MessageData handle(ApiException apiException) {
                MessageData data = new MessageData();
                data.setMessage(apiException.getMessage());
                return data;
        }

        
        /** 
         * Throwable handler
         * @param exception
         * @return MessageData
         */
        @ExceptionHandler(Throwable.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public MessageData handle(Throwable exception) {
                MessageData data = new MessageData();
                data.setMessage("An unknown error has occurred - " + exception.getMessage());
                return data;
        }


        
        /** 
         * ConstraintViolationException handler
         * @param constraintException
         * @return MessageData
         */
        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public final MessageData handleConstraintViolation(ConstraintViolationException constraintException) {
                List<String> details = constraintException.getConstraintViolations()
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

        
        /** 
         * InitBinder to register custom parsers for date time types in the request parameters
         * @param binder
         */
        @InitBinder
        public void initBinder(WebDataBinder binder) {
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
