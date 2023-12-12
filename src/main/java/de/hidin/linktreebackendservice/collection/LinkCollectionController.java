package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collection")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LinkCollectionController {

    private final LinkCollectionService linkCollectionService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LinkCollectionRequest linkCollectionRequest) {
        String slug = linkCollectionRequest.getSlug();
        if (linkCollectionService.slugExists(slug)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 Conflict
                    .body("Error: Slug already exists");
        }

        LinkCollection linkCollection = linkCollectionService.createLinkCollection(slug);
        return ResponseEntity.ok(linkCollection);
    }

//    @GetMapping("/get-all")
//    public ResponseEntity<List<LinkEntry>> getLinkById() {
//        List<LinkEntry> linkEntries = linkEntryService.getAllLinkEntires();
//        if (linkEntries != null && !linkEntries.isEmpty()) {
//            return ResponseEntity.ok(linkEntries);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
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
