package com.modul.sherlock.configs;

import com.modul.sherlock.models.Post;
import com.modul.sherlock.models.User;
import com.modul.sherlock.repos.PostRepo;
import com.modul.sherlock.repos.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private PostRepo postRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostService postService;

    @Test
    void createUser() {
        User user = new User();
        boolean isUserCreated = userService.createUser(user);
        Assert.assertTrue(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    void createUserFail() {
        User user = new User();
        user.setEmail("sherlock@gmail.com");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByEmail("sherlock@gmail.com");
        boolean isUserCreated = userService.createUser(user);
        Assert.assertFalse(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void ReturnAllPostsWhenTitleIsNull() {
        List<Post> posts = Arrays.asList(new Post(), new Post(), new Post());
        Mockito.when(postRepo.findAll()).thenReturn(posts);
        List<Post> result = postService.listPosts(null);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldSetUserForPostAndSaveIt() {
        Post post = new Post();
        Principal principal = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        User user = new User();
        user.setEmail("user@example.com");
        Mockito.when(userRepo.findByEmail("user@example.com")).thenReturn(user);
        postService.savePost(principal, post);
        Assertions.assertEquals(user, post.getUser());
        Mockito.verify(postRepo, Mockito.times(1)).save(post);
    }

    @Test
    public void shouldDeletePostWithSpecifiedId() {
        Long id = 1L;
        postService.removePost(id);
        Mockito.verify(postRepo, Mockito.times(1)).deleteById(id);
    }

}