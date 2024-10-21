package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> updateRole(Long id, Role roleDetails) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setNom_role(roleDetails.getNom_role());
                    existingRole.setRequiresDivision(roleDetails.isRequiresDivision());
                    existingRole.setRequiresPole(roleDetails.isRequiresPole());
                    existingRole.setRedirectionLink(roleDetails.getRedirectionLink());
                    return roleRepository.save(existingRole);
                });
    }

    public boolean deleteRole(Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.delete(role);
                    return true;
                }).orElse(false);
    }
}
