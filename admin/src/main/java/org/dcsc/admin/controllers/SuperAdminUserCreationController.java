package org.dcsc.admin.controllers;

import org.dcsc.core.user.DcscUserCreationForm;
import org.dcsc.core.user.DcscUserCreationFormValidator;
import org.dcsc.core.user.DcscUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by tktong on 8/4/2015.
 */
@Controller
@RequestMapping("/admin/super/users/create")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SuperAdminUserCreationController {
    @Autowired
    private DcscUserService dcscUserService;
    @Autowired
    private DcscUserCreationFormValidator createUserFormValidator;

    @InitBinder("dcscUserCreationForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(createUserFormValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String createUser(Model model) {
        model.addAttribute("form", new DcscUserCreationForm());

        model.addAttribute("content_fragment", DcscUserCreationForm.THYMELEAF_FRAGMENT);

        return "admin/super/user/user-create-form";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute DcscUserCreationForm dcscUserCreationForm, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:/admin/super/users";
        }

        dcscUserService.save(dcscUserCreationForm);


        return "redirect:/admin/super/users";
    }
}