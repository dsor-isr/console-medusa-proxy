//package com.yebisu.medusa.config;
//
//import feign.codec.Decoder;
//import feign.codec.Encoder;
//import feign.jaxb.JAXBContextFactory;
//import feign.soap.SOAPDecoder;
//import feign.soap.SOAPEncoder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.xml.bind.JAXBException;
//
//@Configuration
//public class OpenFeignConfig {
////    @Primary
////    @Bean
////    public Encoder request() throws JAXBException {
////        JAXBContextFactory encoder = new JAXBContextFactory.Builder()
////                .withMarshallerJAXBEncoding("UTF-8")
////                .withMarshallerFormattedOutput(true)
////                .build();
////        encoder.createMarshaller(Request.class);
////        return new SOAPEncoder(encoder);
////    }
//
//
////    @Primary
////    @Bean
////    public Decoder response() throws JAXBException {
////        JAXBContextFactory decoder = new JAXBContextFactory.Builder()
////                .withMarshallerJAXBEncoding("UTF-8")
////                .build();
////        decoder.createUnmarshaller(Object.class);
////        return new SOAPDecoder(decoder);
////    }
//
////    @Bean
////    public Encoder requestInstr() throws JAXBException {
////        JAXBContextFactory  encoder = new JAXBContextFactory.Builder()
////                .withMarshallerJAXBEncoding("UTF-8")
////                .withMarshallerFormattedOutput(true)
////                .build();
////        encoder.createMarshaller(RequestInstr.class);
////        return new SOAPEncoder(encoder);
////    }
////
////    @Bean
////    public Decoder responseInstr() throws JAXBException {
////        JAXBContextFactory decoder = new JAXBContextFactory.Builder()
////                .withMarshallerJAXBEncoding("UTF-8")
////                .build();
////        decoder.createUnmarshaller(ResponseInstr.class);
////        return new SOAPDecoder(decoder);
////    }
//
//}
