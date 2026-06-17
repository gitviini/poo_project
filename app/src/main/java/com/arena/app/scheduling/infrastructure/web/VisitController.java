package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.scheduling.application.dto.VisitRequestDTO;
import com.arena.app.scheduling.application.service.VisitMapper;
import com.arena.app.scheduling.domain.model.Visit;
import com.arena.app.scheduling.domain.repository.VisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;
 
import java.util.UUID;

@Controller
@RequestMapping("/admin/visit")
public class VisitController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitMapper visitMapper;

    @GetMapping("/new")
    public String showCreateForm(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        model.addAttribute("visit", new VisitRequestDTO());
        return "admin/visit-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        var visitOpt = visitRepository.findById(id);
        if (visitOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Visita não encontrada", "statusCode", 404));
            return "redirect:/admin/dashboard";
        }
        
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        model.addAttribute("visit", visitMapper.toDTO(visitOpt.get()));
        return "admin/visit-form";
    }

    @PostMapping("/save")
    public String saveVisit(@ModelAttribute("visit") VisitRequestDTO visitDTO, RedirectAttributes redirectAttributes) {
        boolean isNew = visitDTO.getId() == null;
        
        Visit visit;
        if (isNew) {
            visit = visitMapper.toEntity(visitDTO);
        } else {
            visit = visitRepository.findById(visitDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid visit Id:" + visitDTO.getId()));
            visitMapper.updateEntity(visit, visitDTO);
        }

        visitRepository.save(visit);
        
        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Visita salva com sucesso!",
                "statusCode", 201));
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteVisit(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        visitRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Visita removida com sucesso!",
                "statusCode", 200));
        return "redirect:/admin/dashboard";
    }
}
