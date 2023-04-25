package com.modul.sherlock.configs;

import com.modul.sherlock.models.Post;
import com.modul.sherlock.models.User;
import com.modul.sherlock.repos.PostRepo;
import com.modul.sherlock.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    public List<Post> listPosts(String title) {
        if(title != null) return postRepo.findByTitle(title);
        return postRepo.findAll();
    }
    public void savePost(Principal principal, Post post) {
        post.setUser(getUserByPrincipal(principal));
        log.info("Saving new post: {}; Author: {}", post.getTitle(), post.getUser().getEmail());
        postRepo.save(post);
    }

    public User getUserByPrincipal(Principal principal) {
        if(principal == null) {
            return new User();
        }
        return userRepo.findByEmail(principal.getName());
    }

    public void removePost(Long id) {
        postRepo.deleteById(id);
    }
    public Post getPostById(Long id) {
        return postRepo.findById(id).orElse(null);
    }
}
