package com.metaverse.msme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScalarDocsController {

    /**
     * server.servlet.context-path=/msme strips /msme before routing.
     * So the browser hits http://localhost:8083/msme/scalar
     * but Spring sees only /scalar internally.
     * We forward to /scalar/index.html so the static file is resolved correctly.
     */
    @GetMapping("/scalar")
    public String scalarDocs() {
        return "forward:/scalar/index.html";
    }
}
