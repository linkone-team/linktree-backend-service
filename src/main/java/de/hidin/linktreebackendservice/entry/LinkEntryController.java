package de.hidin.linktreebackendservice.entry;

import de.hidin.linktreebackendservice.entity.LinkEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LinkEntryController {

    private final LinkEntryService linkEntryService;

    @PostMapping("/search")
    public ResponseEntity<Page<LinkEntry>> searchLinkEntries(@RequestBody SearchCriteria criteria, Pageable pageable) {
        Page<LinkEntry> linkEntries = linkEntryService.searchLinkEntries(criteria, pageable);
        if (linkEntries.hasContent()) {
            return ResponseEntity.ok(linkEntries);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteLinkEntry(@PathVariable Long id) {
        boolean isDeleted = linkEntryService.deleteLinkEntry(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<LinkEntry> updateLinkEntry(@PathVariable Long id, @RequestBody LinkEntryUpdateRequest updateRequest) {
        System.out.println("Hello World! " + id);
        LinkEntry updatedLinkEntry = linkEntryService.updateLinkEntry(id, updateRequest.getTargetUrl(), updateRequest.getDisplayText());
        if (updatedLinkEntry != null) {
            return ResponseEntity.ok(updatedLinkEntry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
