package com.epam.esm.controller;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.certificate.CertificateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gift_certificate")
public class CertificateController {
    private GiftCertificateDao giftCertificateDao;
    private CertificateServiceI service;

    @Autowired
    public CertificateController(GiftCertificateDaoImplementation giftCertificateDao,
                                 CertificateService service) {
        this.giftCertificateDao = giftCertificateDao;
        this.service = service;
    }

    @GetMapping("")
    public List<GiftCertificate> showAll() {
        return service.read();
    }

    @PostMapping("")
    public String createNew(@ModelAttribute("gift_certificate") GiftCertificate giftCertificate,
                            BindingResult bindingResult) {
        service.create(giftCertificate);
        return "/gift_sertificate/show_all";
    }

    @GetMapping("/{id}")
    public GiftCertificate show(@PathVariable("id") int id) {
        return service.read(id);
    }

    @PostMapping("/{id}")
    public String addTag(@ModelAttribute("tag") Tag tag,
                         @PathVariable("id") long id,
                         BindingResult bindingResult){

        service.update(tag,giftCertificateDao.read(id));
        return "/gift_sertificate/show";
    }


}
