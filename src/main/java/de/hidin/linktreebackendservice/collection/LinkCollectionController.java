package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collection")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LinkCollectionController {

    private final LinkCollectionService linkCollectionService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LinkCollectionRequest linkCollectionRequest) {
        String slug = linkCollectionRequest.getSlug();
        if (slug == "all") {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 Conflict
                    .body("Error: Slug all can't be used.");
        }
        if (linkCollectionService.slugExists(slug)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 Conflict
                    .body("Error: Slug already exists");
        }

        LinkCollection linkCollection = linkCollectionService.createLinkCollection(slug);
        return ResponseEntity.ok(linkCollection);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getLinkById() {
        List<LinkCollection> linkCollections = linkCollectionService.getAll();
        if (linkCollections != null && !linkCollections.isEmpty()) {
            return ResponseEntity.ok(linkCollections);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<LinkEntry> getLinkById(@PathVariable Long id) {
//        LinkEntry link = linkEntryService.getLinkEntryById(id);
//        if (link != null) {
//            return ResponseEntity.ok(link);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
