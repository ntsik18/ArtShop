package com.ecommerce.ArtShop.Controller;

import com.ecommerce.ArtShop.DTO.PaintingDTO.NewPaintingResponse;
import com.ecommerce.ArtShop.DTO.AuthDTO.AuthenticationRequest;
import com.ecommerce.ArtShop.DTO.AuthDTO.RegisterRequest;
import com.ecommerce.ArtShop.Service.AuthService;
import com.ecommerce.ArtShop.Service.PaintingService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view")
public class ViewController {

    private final AuthService authService;
    private final PaintingService paintingService;

    @GetMapping("/registration")
    public String registerView(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "getregistered";

    }

    @PostMapping("/registration")
    public String register(
            @Valid RegisterRequest request,
            BindingResult result,
            Model model

    ) throws MessagingException {
        authService.checkForExistingUser(request, result);
        if (result.hasErrors()) {
            model.addAttribute("user", request);
            return "redirect:/view/registration?error";
        }
        authService.register(request);
        return "redirect:/view/registration?success";
    }

    @GetMapping("/activation")
    public String confirm(
            @RequestParam String token
    ) throws MessagingException {
        authService.activateAccount(token);
        return "redirect:/view/registration?success\"";
    }

    @GetMapping("/login")
    public String authentication(
            Model model
    ) {
        model.addAttribute("user", new AuthenticationRequest());
        return "login";
    }

    @PostMapping("/login")
    public String toAuthenticate(
            @Valid AuthenticationRequest request,
            BindingResult result,
            Model model
    ) {
        authService.checkForExistingUser(request, result);
        if (result.hasErrors()) {
            System.out.println(result);
            model.addAttribute("user", request);
            return "redirect:/view/login?error";
        }
        authService.authenticate(request);
        return "redirect:/view/login?success";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/search/all")
    public String showAll(Model model) {
        List<NewPaintingResponse> paintings = paintingService.findAllPaintings();
        model.addAttribute("paintings", paintings);
        return "search";
    }
    @GetMapping("/search/image")
    public String showImage() {
        return "image";
    }
}
