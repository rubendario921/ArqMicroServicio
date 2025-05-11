package com.rdcl.Usersapi.service;

import com.rdcl.Usersapi.dto.ClienteDTO;
import com.rdcl.Usersapi.entity.Cliente;
import com.rdcl.Usersapi.exception.DuplicateDataException;
import com.rdcl.Usersapi.exception.IsNullOrEmptyException;
import com.rdcl.Usersapi.exception.ResourceNotFoundException;
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
        if (request == null) throw new IsNullOrEmptyException("ClienteService", "saveData", "Request is null or empty");
        try {
            Cliente existingCliente = clienteRepository.findBycliIdentification(request.getCliIdentification());
            if (existingCliente != null)
                throw new DuplicateDataException("ClienteService", "saveData", "Identification already exists");

            Cliente newCliente = mapTEntity(request);
            Cliente savedCliente = clienteRepository.save(newCliente);
            return CompletableFuture.completedFuture(savedCliente);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Cliente> updateData(Integer id, ClienteDTO request) {
        if (id == null) throw new IsNullOrEmptyException("ClienteService", "updateData", "ID is null");
        if (request == null)
            throw new IsNullOrEmptyException("ClienteService", "updateData", "Request is null or empty");

        try {
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("updateData", "Cliente not found"));
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
        if (id == null) throw new IsNullOrEmptyException("ClienteService", "updateData", "ID is null");
        try {
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("updateData", "Cliente not found"));
            return CompletableFuture.completedFuture(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Error: getDataById Message: {}", e);
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

    @Async
    @Override
    public CompletableFuture<Void> deleteDataById(Integer id) {
        if (id == null) throw new IsNullOrEmptyException("ClienteService", "updateData", "ID is null");
        try {
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("updateData", "Cliente not found"));
            clienteRepository.delete(cliente);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente: " + e.getMessage(), e);
        }
    }

    private static Cliente mapTEntity(ClienteDTO clienteDTO) {
        Cliente newCliente = new Cliente();
        newCliente.setPerName(clienteDTO.getCliName());
        newCliente.setPerGender(clienteDTO.getCliGender());
        newCliente.setPerAge(clienteDTO.getCliAge());
        newCliente.setPerIdentification(clienteDTO.getCliIdentification());
        newCliente.setPerAddress(clienteDTO.getCliAddress());
        newCliente.setPerNumberPhone(clienteDTO.getCliNumberPhone());
        newCliente.setCliPassword(clienteDTO.getCliPassword());
        newCliente.setCliStatus(true);
        return newCliente;
    }

}