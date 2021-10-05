package com.epam.esm.controller;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/gift_certificate")
public class CertificateController {
    private GiftCertificateDao giftCertificateDao;
    private Service service;

    @Autowired
    public CertificateController(GiftCertificateDaoImplementation giftCertificateDao,
                                 CertificateService service) {
        this.giftCertificateDao = giftCertificateDao;
        this.service = service;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("list_of_certificates", giftCertificateDao.read());
        return "/gift_sertificate/show_all";
    }

    @PostMapping("")
    public String createNew(@ModelAttribute("gift_certificate") GiftCertificate giftCertificate,
                            BindingResult bindingResult) {
        giftCertificateDao.create(giftCertificate);
        return "/gift_sertificate/show_all";
    }

    @GetMapping("/new")
    public String showNew(@ModelAttribute("gift_certificate") GiftCertificate giftCertificate) {
        return "/gift_sertificate/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("certificate", giftCertificateDao.read(id));
        return "/gift_sertificate/show";
    }

    @PostMapping("/{id}")
    public String addTag(@ModelAttribute("tag") Tag tag,
                         @PathVariable("id") long id,
                         BindingResult bindingResult){
        //service.create(tag,giftCertificateDao.read(id));
        return "/gift_sertificate/show";
    }


}
