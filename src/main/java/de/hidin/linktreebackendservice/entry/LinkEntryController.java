package de.hidin.linktreebackendservice.entry;

import de.hidin.linktreebackendservice.entity.LinkEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LinkEntryController {

    private final LinkEntryService linkEntryService;

    @PostMapping("/create")
    public LinkEntry createLink(@RequestBody LinkEntryRequest linkEntryRequest) {
        String targetUrl = linkEntryRequest.getTargetUrl();
        String displayText = linkEntryRequest.getDisplayText();
        return linkEntryService.createLinkEntry(targetUrl, displayText);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<LinkEntry>> getLinkById() {
        List<LinkEntry> linkEntries = linkEntryService.getAllLinkEntires();
        if (linkEntries != null && !linkEntries.isEmpty()) {
            return ResponseEntity.ok(linkEntries);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<LinkEntry> getLinkById(@PathVariable Long id) {
        LinkEntry link = linkEntryService.getLinkEntryById(id);
        if (link != null) {
            return ResponseEntity.ok(link);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
