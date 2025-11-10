package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.utils.dto.ImagenDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T00:14:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class ImagenMapperImpl implements ImagenMapper {

    @Override
    public List<ImagenDTO> toDTOList(Collection<Imagen> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ImagenDTO> list = new ArrayList<ImagenDTO>( entities.size() );
        for ( Imagen imagen : entities ) {
            list.add( toDTO( imagen ) );
        }

        return list;
    }

    @Override
    public List<Imagen> toEntityList(List<ImagenDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Imagen> list = new ArrayList<Imagen>( dtos.size() );
        for ( ImagenDTO imagenDTO : dtos ) {
            list.add( toEntity( imagenDTO ) );
        }

        return list;
    }

    @Override
    public ImagenDTO toDTO(Imagen entity) {
        if ( entity == null ) {
            return null;
        }

        ImagenDTO.ImagenDTOBuilder imagenDTO = ImagenDTO.builder();

        imagenDTO.nombre( entity.getNombre() );
        imagenDTO.mime( entity.getMime() );
        byte[] contenido = entity.getContenido();
        if ( contenido != null ) {
            imagenDTO.contenido( Arrays.copyOf( contenido, contenido.length ) );
        }
        imagenDTO.tipoImagen( entity.getTipoImagen() );

        return imagenDTO.build();
    }

    @Override
    public Imagen toEntity(ImagenDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Imagen.ImagenBuilder imagen = Imagen.builder();

        imagen.nombre( dto.getNombre() );
        imagen.mime( dto.getMime() );
        byte[] contenido = dto.getContenido();
        if ( contenido != null ) {
            imagen.contenido( Arrays.copyOf( contenido, contenido.length ) );
        }
        imagen.tipoImagen( dto.getTipoImagen() );

        return imagen.build();
    }
}
