package com.rdcl.Usersapi.service;

import com.rdcl.Usersapi.dto.ClienteDTO;
import com.rdcl.Usersapi.entity.Cliente;
import com.rdcl.Usersapi.repository.ClienteRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ClienteService implements IClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Async
    @Override
    public CompletableFuture<Cliente> saveData(ClienteDTO request) {
        if (request == null) throw new RuntimeException("ClienteRequest is null");
        try {

            Cliente newCliente = new Cliente();
            newCliente.setPerName(request.getCliName());
            newCliente.setPerGender(request.getCliGender());
            newCliente.setPerAge(request.getCliAge());
            newCliente.setPerIdentification(request.getCliIdentification());
            newCliente.setPerAddress(request.getCliAddress());
            newCliente.setPerNumberPhone(request.getCliNumberPhone());

            newCliente.setCliPassword(request.getCliPassword());
            newCliente.setCliStatus(true);

            Cliente savedCliente = clienteRepository.save(newCliente);
            return CompletableFuture.completedFuture(savedCliente);
        } catch (Exception e) {
            throw new RuntimeException("Error: saveData Message:", e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Cliente> updateData(Integer id, ClienteDTO request) {
        if (id == null) throw new RuntimeException("ID is null");
        if (request == null) throw new RuntimeException("ClienteRequest is null");

        try {
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente not found"));

            cliente.setPerName(request.getCliName());
            cliente.setPerIdentification(request.getCliIdentification());
            cliente.setPerAddress(request.getCliAddress());
            cliente.setPerNumberPhone(request.getCliNumberPhone());
            cliente.setPerGender(request.getCliGender());
            cliente.setPerAge(request.getCliAge());

            cliente.setCliPassword(request.getCliPassword());
            cliente.setCliStatus(true);

            Cliente updateCliente = clienteRepository.save(cliente);
            return CompletableFuture.completedFuture(updateCliente);


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @Async
    @Override
    public CompletableFuture<Cliente> getDataById(Integer id) {
        if (id == null) throw new RuntimeException("ID is null");
        try {
            Cliente cliente = clienteRepository.findById(id).orElse(null);
            return CompletableFuture.completedFuture(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Error: getDataById Message:", e);
        }
    }

    @Async
    @Override
    public CompletableFuture<List<Cliente>> getAllData() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            return CompletableFuture.completedFuture(clientes);
        } catch (Exception e) {
            throw new RuntimeException("Error: getAllData Message:", e);
        }
    }


    @Override
    public void deleteDataById(Integer id) {
        if (id == null) throw new RuntimeException("ID is null");
        try {
            clienteRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error: deleteDataById Message:", e);
        }
    }

}