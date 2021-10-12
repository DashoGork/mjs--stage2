package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.certificate.CertificateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private CertificateServiceI service;

    @Autowired
    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping
    public List<GiftCertificate> showAll() {
        return service.read();
    }

    @PostMapping
    public List<GiftCertificate> createNew(@RequestBody GiftCertificate giftCertificate) {
        service.create(giftCertificate);
        return service.read();
    }


    @GetMapping("/{id}")
    public GiftCertificate show(@PathVariable("id") int id) {
        return service.read(id);
    }

    @PatchMapping("/{id}")
    public List<GiftCertificate> update(@PathVariable Long id,
                                        @RequestBody GiftCertificate patchedCertificate) {
        service.patch(id, patchedCertificate);
        return service.read();
    }

    @GetMapping("/query")
    public List<GiftCertificate> showByQueryOrTag(@RequestBody Map<String, String> params) {
        return service.getByTagOrQueryAndSort(params.get("query"), params.get("sortField"),
                params.get("sortOrder"), params.get("tagName"));
    }

    @DeleteMapping("/{id}")
    public List<GiftCertificate> delete(@PathVariable("id") int id) {
        service.delete(service.read(id));
        return service.read();
    }
}