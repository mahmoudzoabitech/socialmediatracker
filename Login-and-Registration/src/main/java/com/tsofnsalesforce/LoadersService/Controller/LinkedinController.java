import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/linkedin")
public class LinkedinController {

    private final LinkedinRepository linkedinRepository;

    @Autowired
    public LinkedinController(LinkedinRepository linkedinRepository) {
        this.linkedinRepository = linkedinRepository;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<LinkedinPost>> getAllPosts() {
        List<LinkedinPost> posts = linkedinRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<LinkedinPost> createPost(@RequestBody LinkedinPost post) {
        LinkedinPost savedPost = linkedinRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<LinkedinPost> getPostById(@PathVariable Long id) {
        LinkedinPost post = linkedinRepository.findById(id)
                .orElse(null);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<LinkedinPost> updatePost(@PathVariable Long id, @RequestBody LinkedinPost updatedPost) {
        LinkedinPost existingPost = linkedinRepository.findById(id)
                .orElse(null);
        if (existingPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        // כאן נמשיך עם עדכון שאר השדות בהתאם לצורך
        LinkedinPost savedPost = linkedinRepository.save(existingPost);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
        try {
            linkedinRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
