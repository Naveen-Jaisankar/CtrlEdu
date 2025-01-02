package com.ctrledu.AuthService.service;

import com.ctrledu.AuthService.dto.ClassRequest;
import com.ctrledu.AuthService.entity.ClassEntity;
import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.OrganizationEntity;
import com.ctrledu.AuthService.repository.ClassRepository;
import com.ctrledu.AuthService.repository.ModuleRepository;
import com.ctrledu.AuthService.repository.OrganizationRepository;
import com.ctrledu.AuthService.response.ClassResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<ClassResponse> getClassesByOrgId(Long orgId) {
        List<ClassEntity> classes = classRepository.findByOrgOrgId(orgId);
        return classes.stream()
                .map(cls -> new ClassResponse(
                        cls.getClassId(),
                        cls.getClassName(),
                        cls.getNumStudents(),
                        cls.getModules().stream().map(ModuleEntity::getModuleName).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public void addClass(ClassRequest request, Long orgId) {
        OrganizationEntity org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found."));
        List<ModuleEntity> modules = moduleRepository.findAllById(request.getModuleIds());

        ClassEntity cls = new ClassEntity();
        cls.setClassName(request.getClassName());
        cls.setNumStudents(request.getNumStudents());
        cls.setOrg(org);
        cls.setModules(modules);

        classRepository.save(cls);
    }
}

