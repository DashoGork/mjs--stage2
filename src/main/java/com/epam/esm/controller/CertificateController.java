package com.epam.esm.controller;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.dto.certificate.CertificateDtoService;
import com.epam.esm.service.dto.certificate.CertificateDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private CertificateDtoServiceI service;

    @Autowired
    public CertificateController(CertificateDtoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CertificateDto> showAll() {
        return service.read();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto createNew(@Validated @RequestBody CertificateDto certificate) {
        return service.create(certificate);
    }

    @GetMapping("/{id}")
    public CertificateDto show(@PathVariable("id") int id) {
        return service.read(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto update(@PathVariable Long id,
                                 @Validated @RequestBody CertificateDto patchedCertificate) {

        service.patch(id, patchedCertificate);
        return service.read(id);
    }

    @GetMapping("/query")
    public List<CertificateDto> showByQueryOrTag(
            @RequestParam(defaultValue = "", required = false) String query,
            @RequestParam(defaultValue = "", required = false) String sortField,
            @RequestParam(defaultValue = "", required = false) String sortOrder,
            @RequestParam(defaultValue = "", required = false) String tagName
    ) {
        return service.getByTagOrQueryAndSort(query, sortField,
                sortOrder, tagName);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        service.delete(service.read(id));
    }
}