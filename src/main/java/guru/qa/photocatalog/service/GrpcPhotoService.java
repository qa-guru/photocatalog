package guru.qa.photocatalog.service;

import com.google.protobuf.util.Timestamps;
import guru.qa.grpc.photocatalog.CountRequest;
import guru.qa.grpc.photocatalog.IdRequest;
import guru.qa.grpc.photocatalog.PhotoRequest;
import guru.qa.grpc.photocatalog.PhotoResponse;
import guru.qa.grpc.photocatalog.PhotocatalogServiceGrpc;
import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.domain.graphql.PhotoInputGql;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GrpcPhotoService extends PhotocatalogServiceGrpc.PhotocatalogServiceImplBase {

  private static final Random rand = new Random();
  private final PhotoService photoService;

  public GrpcPhotoService(PhotoService photoService) {
    this.photoService = photoService;
  }

  @Override
  public void photo(IdRequest request, StreamObserver<PhotoResponse> responseObserver) {
    final PhotoGql photoGql = photoService.photoGqlById(request.getId());
    responseObserver.onNext(
        PhotoResponse.newBuilder()
            .setId(photoGql.id().toString())
            .setDescription(photoGql.description())
            .setLastModifyDate(Timestamps.fromDate(photoGql.lastModifyDate()))
            .setContent(photoGql.content())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void addPhoto(PhotoRequest request, StreamObserver<PhotoResponse> responseObserver) {
    PhotoGql photoGql = photoService.addPhotoGql(new PhotoInputGql(
        request.getDescription(), request.getContent()
    ));
    responseObserver.onNext(
        PhotoResponse.newBuilder()
            .setId(photoGql.id().toString())
            .setDescription(photoGql.description())
            .setLastModifyDate(Timestamps.fromDate(photoGql.lastModifyDate()))
            .setContent(photoGql.content())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void randomPhotos(CountRequest request, StreamObserver<PhotoResponse> responseObserver) {
    List<PhotoGql> photos = photoService.allGqlPhotos();
    for (int i = 0; i < request.getCount(); i++) {
      PhotoGql randomPhoto = photos.get(rand.nextInt(photos.size()));
      responseObserver.onNext(
          PhotoResponse.newBuilder()
              .setId(randomPhoto.id().toString())
              .setDescription(randomPhoto.description())
              .setLastModifyDate(Timestamps.fromDate(randomPhoto.lastModifyDate()))
              .setContent(randomPhoto.content())
              .build());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    responseObserver.onCompleted();
  }
}
