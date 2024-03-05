package org.example.springlearning.controllers;

import io.micrometer.common.KeyValue;
import org.example.springlearning.models.Post;
import org.example.springlearning.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BlogAPI {

    @GetMapping(path = "api")
    public List<String> list(){
        return List.of("Hello", "world") ;
    }

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/blog")
    public Iterable<Post> blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        return posts;
    }


    @PostMapping("/api/blog/add")
    public String blogPostAdd(@RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);

        return "{ \n\"title\": " + title + ",\n\"anons\": " + anons + ",\n\"full_text\": " + full_text + "\n}";
    }

    @GetMapping("/api/blog/{id}")
    public ArrayList<Post> blogDetail(Model model, @PathVariable(value = "id") long id){
        ArrayList<Post> res = new ArrayList<>();
        if(!postRepository.existsById(id)) {
            return res;//"{\"Error\": В базе данных нет поста с таким ID. }";
        }
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(res::add);
        return res;
    }

    @GetMapping("/api/blog/{id}/edit")
    public ArrayList<Post> blogEdit(Model model, @PathVariable(value = "id") long id){
        return blogDetail(model, id);
    }

    @PutMapping("/api/blog/{id}/edit")
    public String blogPostUpdate(Model model,
                                 @PathVariable(value = "id") long id,
                                 @RequestParam String title,
                                 @RequestParam String anons,
                                 @RequestParam String full_text){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "{ \n\"title\": " + title + ",\n\"anons\": " + anons + ",\n\"full_text\": " + full_text + "\n}";
    }

    @DeleteMapping("/api/blog/{id}/remove")
    public String blogDelete(Model model, @PathVariable(value = "id") long id){
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "{ \"message\": Запись удалена }";
    }

}
