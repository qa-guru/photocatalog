package guru.qa.photocatalog.service;

import guru.qa.photocatalog.data.PhotoRepository;
import guru.qa.photocatalog.domain.Photo;
import guru.qa.photocatalog.ex.PhotoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DbPhotoService implements PhotoService {

  private final PhotoRepository photoRepository;

  @Autowired
  public DbPhotoService(PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
  }

  @Override
  public List<Photo> allPhotos() {
    return photoRepository.findAll()
        .stream()
        .map(fe -> new Photo(
            fe.getDescription(),
            fe.getLastModifyDate(),
            fe.getContent() != null ? new String(fe.getContent()) : ""
        )).toList();
  }

  @Override
  public Photo photoByDescription(String description) {
    return null;
  }

  @Override
  public Photo byId(String id) {
    return photoRepository.findById(UUID.fromString(id))
        .map(fe -> new Photo(
            fe.getDescription(),
            fe.getLastModifyDate(),
            fe.getContent() != null ? new String(fe.getContent()) : ""
        )).orElseThrow(PhotoNotFoundException::new);
  }
}
