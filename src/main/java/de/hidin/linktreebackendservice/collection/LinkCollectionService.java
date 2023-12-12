package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkCollectionService {
    private final LinkCollectionRepository collectionRepository;

    @Autowired
    public LinkCollectionService(LinkCollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public LinkCollection createLinkCollection(String slug) {
        LinkCollection collection = new LinkCollection();
        collection.setSlug(slug);
        return collectionRepository.save(collection);
    }

    public LinkCollection getLinkCollectionBySlug(String slug) {
        return collectionRepository.findBySlug(slug).orElse(null);
    }

    public boolean slugExists(String slug) {
        return getLinkCollectionBySlug(slug) != null;
    }

    public List<LinkCollection> getAll() {
        return collectionRepository.findAll();
    }


    // Add more methods for CRUD operations or custom queries as needed
}
