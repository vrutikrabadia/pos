package com.increff.pos.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ClientWrapper {
    @Autowired
    private PdfClient pdfClient;
}
