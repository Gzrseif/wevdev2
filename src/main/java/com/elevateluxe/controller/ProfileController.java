package com.elevateluxe.controller;

import com.elevateluxe.dto.ProfileDto;
import com.elevateluxe.entity.User;
import com.elevateluxe.service.FileStorageService;
import com.elevateluxe.service.OrderService;
import com.elevateluxe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final OrderService orderService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        ProfileDto dto = new ProfileDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setCountry(user.getCountry());
        dto.setPostalCode(user.getPostalCode());
        model.addAttribute("user", user);
        model.addAttribute("profileDto", dto);
        model.addAttribute("recentOrders", orderService.findByUser(user).stream().limit(3).toList());
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("profileDto") ProfileDto dto,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
            return "user/profile";
        }
        userService.updateProfile(userDetails.getUsername(), dto);
        redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully.");
        return "redirect:/profile";
    }

    @PostMapping("/avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            String url = fileStorageService.storeFile(file);
            userService.updateAvatar(userDetails.getUsername(), url);
            redirectAttributes.addFlashAttribute("successMsg", "Profile photo updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Could not upload image.");
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("pwError", "New passwords do not match.");
            return "redirect:/profile";
        }
        try {
            userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMsg", "Password changed successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("pwError", e.getMessage());
        }
        return "redirect:/profile";
    }
}
