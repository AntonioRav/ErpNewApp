package com.eval.newApp.controller.Import;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;

import java.nio.file.Path;
import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import com.eval.newApp.model.*;
import com.eval.newApp.service.Import.*;

import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @GetMapping("/importPage")
    public String importPage() {
        return "import";
    }

    @PostMapping("/importer")
    public String importer(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,Model model) {
        try {
            Map response = importService.importCsvFiles(
                file1.getBytes(), file2.getBytes(), file3.getBytes()
            );
            model.addAttribute("message", response);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur interne : " + e.getMessage());
        }

        return "import"; // la page HTML à afficher
    }
}

