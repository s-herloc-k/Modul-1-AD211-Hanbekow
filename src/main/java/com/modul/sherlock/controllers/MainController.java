package com.modul.sherlock.controllers;

import com.modul.sherlock.configs.PostService;
import com.modul.sherlock.models.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        return "home";
    }

    @GetMapping("/blog")
    public String blog(Principal principal, @RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        model.addAttribute("posts", postService.listPosts(title));
        return "blog";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Principal principal, Model model) {
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(Principal principal, Post post) {
        postService.savePost(principal, post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostRemove(@PathVariable(value = "id") Long id) {
        postService.removePost(id);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogInfo(Principal principal, @PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        model.addAttribute("post", postService.getPostById(id));
        return "blog-more";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(Principal principal, @PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        model.addAttribute("postEdit", postService.getPostById(id));
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(Principal principal, @PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons, @RequestParam String text, Model model) {
        Post post = postService.getPostById(id);
        post.setTitle(title);
        post.setAnons(anons);
        post.setText(text);
        postService.savePost(principal, post);
        return "redirect:/blog";
    }
}
