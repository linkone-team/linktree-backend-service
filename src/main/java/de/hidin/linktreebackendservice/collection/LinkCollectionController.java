package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import de.hidin.linktreebackendservice.entity.LinkEntry;
import de.hidin.linktreebackendservice.entry.LinkEntryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/collection")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LinkCollectionController {

    private final LinkCollectionService linkCollectionService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LinkCollectionRequest linkCollectionRequest) {
        String slug = linkCollectionRequest.getSlug();
        String slugRegex = "^[a-z0-9]+(-[a-z0-9]+)*$";

        if (slug == "all") {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 Conflict
                    .body("Error: Slug all can't be used.");
        }
        if (!Pattern.matches(slugRegex, slug)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                    .body("Error: Invalid slug format.");
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
    public ResponseEntity<?> getAllWallets(Pageable pageable) {
        Page<LinkCollection> linkCollections = linkCollectionService.getAll(pageable);
        if (linkCollections != null && linkCollections.hasContent()) {
            return ResponseEntity.ok(linkCollections);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{slugPart}")
    public ResponseEntity<?> getWalletsBySlugPart(@PathVariable String slugPart, Pageable pageable) {
        Page<LinkCollection> linkCollections = linkCollectionService.getBySlugStartsWith(slugPart, pageable);
        if (linkCollections != null && linkCollections.hasContent()) {
            return ResponseEntity.ok(linkCollections);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWalletById(@PathVariable Long id) {
        boolean isDeleted = linkCollectionService.deleteById(id);
        if (isDeleted) {
            return ResponseEntity.ok().body("Wallet with ID: " + id + " has been successfully deleted");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error: Wallet with ID: " + id + " not found.");
        }
    }

    @PostMapping("/{slug}/add-link")
    public ResponseEntity<?> addLinkToCollection(@PathVariable String slug,
                                                 @RequestBody LinkEntryRequest linkEntryRequest) {
        try {
            LinkEntry linkEntry = linkCollectionService.addLinkToCollection(slug, linkEntryRequest);
            return ResponseEntity.ok(linkEntry);
        } catch (Exception e) {
            // Handle exceptions such as collection not found, etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{slug}/links")
    public ResponseEntity<Page<LinkEntry>> getLinksForCollection(@PathVariable String slug, Pageable pageable) {
        try {
            Page<LinkEntry> links = linkCollectionService.getAllLinksForCollection(slug, pageable);
            return ResponseEntity.ok(links);
        } catch (Exception e) {
            // Handle exceptions such as collection not found, etc.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
