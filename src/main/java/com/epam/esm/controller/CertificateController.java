package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.certificate.CertificateServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
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
    public List<GiftCertificate> createNew(@RequestBody GiftCertificate giftCertificate,
                                           @RequestBody List<Tag> tags) {
        String g = giftCertificate.getDescription();
        Tag tag = tags.get(0);
        service.create(giftCertificate, tag);
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
        Tag tag=new Tag();
        service.update(tag, giftCertificate);
        return service.read();
    }

    @GetMapping("/query/{query}")
    public List<GiftCertificate> showByQuery(@PathVariable("query") String query) {
        return service.getByPartOfNameOrDescription(query);
    }

    @GetMapping("/tag/{tagName}")
    public List<GiftCertificate> showByTag(@PathVariable("tagName") String tagName) {
        return service.getAllCertificatesByTagName(tagName);
    }

    @GetMapping("/sort/{query}/{sortField}/{sortOrder}")
    public List<GiftCertificate> showBySort(@PathVariable("sortField") String sortField,
                                            @PathVariable("query") String query,
                                            @PathVariable("sortOrder") String sortOrder) {
        return service.sortByAscDesc(query, sortField, sortOrder);
    }

    @PostMapping("/{id}")
    public String addTag(@ModelAttribute("gift") GiftCertificate giftCertificate,
                         @PathVariable("id") long id,
                         BindingResult bindingResult) {
        System.out.println(giftCertificate.getDescription());
        //service.update(tag,giftCertificateDao.read(id));
        return "/gift_sertificate/show";
    }

    @DeleteMapping("/{id}")
    public List<GiftCertificate> delete(@PathVariable("id") int id) {
        service.delete(service.read(id));
        return service.read();
    }
}
