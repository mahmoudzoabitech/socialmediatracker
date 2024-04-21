import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    private final InstagramRepository instagramRepository;

    @Autowired
    public InstagramController(InstagramRepository instagramRepository) {
        this.instagramRepository = instagramRepository;
    }

    @GetMapping("/photos")
    public ResponseEntity<List<InstagramPhoto>> getAllPhotos() {
        List<InstagramPhoto> photos = instagramRepository.findAll();
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @PostMapping("/photos")
    public ResponseEntity<InstagramPhoto> createPhoto(@RequestBody InstagramPhoto photo) {
        InstagramPhoto savedPhoto = instagramRepository.save(photo);
        return new ResponseEntity<>(savedPhoto, HttpStatus.CREATED);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<InstagramPhoto> getPhotoById(@PathVariable Long id) {
        InstagramPhoto photo = instagramRepository.findById(id)
                .orElse(null);
        if (photo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @PutMapping("/photos/{id}")
    public ResponseEntity<InstagramPhoto> updatePhoto(@PathVariable Long id, @RequestBody InstagramPhoto updatedPhoto) {
        InstagramPhoto existingPhoto = instagramRepository.findById(id)
                .orElse(null);
        if (existingPhoto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingPhoto.setCaption(updatedPhoto.getCaption());
        existingPhoto.setImageUrl(updatedPhoto.getImageUrl());
        InstagramPhoto savedPhoto = instagramRepository.save(existingPhoto);
        return new ResponseEntity<>(savedPhoto, HttpStatus.OK);
    }

    @DeleteMapping("/photos/{id}")
    public ResponseEntity<HttpStatus> deletePhoto(@PathVariable Long id) {
        try {
            instagramRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
