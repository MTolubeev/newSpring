package com.example.EShop.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "contentType")
    private String contentType;
    @Column(name = "isPreviewImage")
    private boolean isPreviewImage;

    //@Lob
    @Column(name = "bytes", columnDefinition="bytea")
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Product product;

    @Override
    public String toString() {
        return "Image{id=" + id + ", name='" + name + ", originalFileName='" + originalFileName + ", size='" + size + ", contentType='" + contentType + ", isPreviewImage='" + isPreviewImage + "'}";
    }
}
