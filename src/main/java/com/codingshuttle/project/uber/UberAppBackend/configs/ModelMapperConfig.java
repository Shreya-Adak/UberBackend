package com.codingshuttle.project.uber.UberAppBackend.configs;

import com.codingshuttle.project.uber.UberAppBackend.dtos.PointDto;
import com.codingshuttle.project.uber.UberAppBackend.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .typeMap(PointDto.class, Point.class)
                .setConverter(context -> {
            PointDto pointDto = context.getSource();
            return GeometryUtil.createPoint(pointDto);

        });
        modelMapper
                .typeMap(Point.class,PointDto.class)
                .setConverter(context ->{
           Point point = context.getSource();
           double[] coordinates ={
                   point.getX(),
                   point.getY()
           };
           return new PointDto(coordinates);
        });

        return modelMapper;
    }



}
