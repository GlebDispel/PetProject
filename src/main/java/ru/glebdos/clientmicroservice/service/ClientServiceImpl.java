package ru.glebdos.clientmicroservice.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.model.Client;
import ru.glebdos.clientmicroservice.repository.ClientRepository;

import java.time.LocalDateTime;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void createClient(ClientDto clientDto) {

        Client localClient = convertClientDtoToClient(clientDto);
        setTimeRegistration(localClient);
        clientRepository.save(localClient);

    }


    private Client convertClientDtoToClient(ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

    private ClientDto convertClientToClientDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    private void setTimeRegistration(Client client) {
        client.setClientRegistrationTime(LocalDateTime.now());
    }
}
