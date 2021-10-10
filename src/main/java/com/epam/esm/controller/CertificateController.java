package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.certificate.CertificateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/gift_certificate")
public class CertificateController {

    private CertificateServiceI service;

    @Autowired
    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<GiftCertificate> showAll() {
        return service.read();
    }

    @PostMapping("")
    public List<GiftCertificate> createNew(@RequestBody GiftCertificate giftCertificate) {
        service.create(giftCertificate);
        return service.read();
    }


    @GetMapping("/{id}")
    public GiftCertificate show(@PathVariable("id") int id) {
        return service.read(id);
    }

    @PatchMapping("/{id}")
    public List<GiftCertificate> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        GiftCertificate giftCertificate = service.read(id);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(GiftCertificate.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, giftCertificate, v);
            field.setAccessible(false);
        });
        Tag tag = new Tag();
        service.update(tag, giftCertificate);
        return service.read();
    }

//    @GetMapping("/query")
//    public List<GiftCertificate> showByQuery(@RequestParam("query") String query) {
//        return service.getByPartOfNameOrDescription(query);
//    }

    @GetMapping("/query")
    public List<GiftCertificate> showByQueryOrTag(@RequestParam("sortField") String sortField,
                                                  @RequestParam("query") String query,
                                                  @RequestParam("sortOrder") String sortOrder,
                                                  @RequestParam("tagName") String tagName) {
        return service.getByTagOrQueryAndSort(query,sortField,sortOrder,tagName);
    }

    @GetMapping("/tag")
    public List<GiftCertificate> showByTag(@RequestParam("tagName") String tagName) {
        return service.getAllCertificatesByTagName(tagName);
    }

    @GetMapping("/sort")
    public List<GiftCertificate> showBySort(@RequestParam("sortField") String sortField,
                                            @RequestParam("query") String query,
                                            @RequestParam("sortOrder") String sortOrder) {
        return service.sortByAscDesc(query, sortField, sortOrder);
    }

    @DeleteMapping("/{id}")
    public List<GiftCertificate> delete(@PathVariable("id") int id) {
        service.delete(service.read(id));
        return service.read();
    }
}