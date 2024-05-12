package com.example.ProductMicroservice.service;


import com.example.ProductMicroservice.rest.CreateProductRestModel;

public interface ProductService {

	String createProduct(CreateProductRestModel productRestModel) throws Exception ;

}
