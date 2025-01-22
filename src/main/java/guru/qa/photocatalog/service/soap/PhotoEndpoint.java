package guru.qa.photocatalog.service.soap;

import guru.qa.photocatalog.config.AppConfig;
import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.service.PhotoService;
import guru.qa.xml.photocatalog.IdRequest;
import guru.qa.xml.photocatalog.PageRequest;
import guru.qa.xml.photocatalog.Photo;
import guru.qa.xml.photocatalog.PhotoResponse;
import guru.qa.xml.photocatalog.PhotosResponse;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class PhotoEndpoint {

  private final PhotoService photoService;

  public PhotoEndpoint(PhotoService photoService) {
    this.photoService = photoService;
  }

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "idRequest")
  @ResponsePayload
  public PhotoResponse photo(@RequestPayload IdRequest request) {
    PhotoGql photo = photoService.photoGqlById(request.getId());
    PhotoResponse response = new PhotoResponse();
    Photo xmlPhoto = new Photo();
    xmlPhoto.setId(photo.id().toString());
    xmlPhoto.setContent(photo.content());
    xmlPhoto.setDescription(photo.description());
    response.setPhoto(xmlPhoto);
    return response;
  }

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "pageRequest")
  @ResponsePayload
  public PhotosResponse photo(@RequestPayload PageRequest request) {
    Page<PhotoGql> photo = photoService.allGqlPhotos(
        org.springframework.data.domain.PageRequest.of(
            request.getPage(),
            request.getSize()
        )
    );
    PhotosResponse response = new PhotosResponse();
    response.setTotalPages(photo.getTotalPages());
    response.setTotalElements(photo.getTotalElements());
    response.getPhotos().addAll(
        photo.getContent().stream().map(
            gqlPhoto -> {
              Photo xmlPhoto = new Photo();
              xmlPhoto.setId(gqlPhoto.id().toString());
              xmlPhoto.setContent(gqlPhoto.content());
              xmlPhoto.setDescription(gqlPhoto.description());
              return xmlPhoto;
            }
        ).toList()
    );
    return response;
  }
}
