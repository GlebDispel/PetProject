package ru.glebdos.clientmicroservice.service;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.model.Client;
import ru.glebdos.clientmicroservice.repository.ClientRepository;


public interface ClientService {


     void createClient(ClientDto clientDto);


}
