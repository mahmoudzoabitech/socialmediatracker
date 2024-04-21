import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facebook")
public class FacebookController {

    private final FacebookRepository facebookRepository;

    @Autowired
    public FacebookController(FacebookRepository facebookRepository) {
        this.facebookRepository = facebookRepository;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<FacebookPost>> getAllPosts() {
        List<FacebookPost> posts = facebookRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<FacebookPost> createPost(@RequestBody FacebookPost post) {
        FacebookPost savedPost = facebookRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<FacebookPost> getPostById(@PathVariable Long id) {
        FacebookPost post = facebookRepository.findById(id)
                .orElse(null);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<FacebookPost> updatePost(@PathVariable Long id, @RequestBody FacebookPost updatedPost) {
        FacebookPost existingPost = facebookRepository.findById(id)
                .orElse(null);
        if (existingPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingPost.setContent(updatedPost.getContent());
        existingPost.setImageUrl(updatedPost.getImageUrl());
        // כאן נמשיך עם עדכון שאר השדות בהתאם לצורך
        FacebookPost savedPost = facebookRepository.save(existingPost);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
        try {
            facebookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
