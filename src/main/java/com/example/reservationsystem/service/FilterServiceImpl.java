package com.example.reservationsystem.service;

import com.example.reservationsystem.domain.Employee;
import com.example.reservationsystem.dto.RoomFilterDto;
import com.example.reservationsystem.dto.mappers.RoomFilterMapper;
import com.example.reservationsystem.security.Role;
import com.example.reservationsystem.service.filters.RecordFilter;
import com.example.reservationsystem.service.filters.rooms.RoomCityIdFilter;
import com.example.reservationsystem.service.filters.rooms.RoomPriorityLowerThanFilter;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FilterServiceImpl implements FilterService {
    private final EmployeeService employeeService;
    private final RoomFilterMapper roomFilterMapper;

    @Override
    public List<RecordFilter> createUserSpecificFilters() {
        List<RecordFilter> recordFilters = new ArrayList<>();

        Employee employee = employeeService.getLoggedInUserDetails();

        addCityRecordFilter(recordFilters, employee);
        addMaxRoomPriorityRecordFilter(recordFilters, employee);

        return recordFilters;
    }

    @Override
    public void addRoomFilter(List<RecordFilter> filters, RoomFilterDto filterDto) {
        filters.add(roomFilterMapper.mapToRoomFilter(filterDto));
        filters.add(roomFilterMapper.mapToTimePeriodFilter(filterDto));
    }

    @Override
    public void enhanceFilterDtoForView(RoomFilterDto dto) {
        List<Boolean> floors = new ArrayList<>();
        floors.add(dto.isFloor1());
        floors.add(dto.isFloor2());
        floors.add(dto.isFloor3());
        floors.add(dto.isFloor4());
        dto.setFloors(floors);
    }

    private void addCityRecordFilter(List<RecordFilter> recordFilters, Employee employee) {
        Collection<GrantedAuthority> authorities = employeeService.getLoggedInUser().getAuthorities();
        boolean admin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + Role.ADMIN.getRoleString()));
        if (!admin) {
            Long delegationCityId = employee.getDelegationCityId();
            Long cityId = employee.getCityId();
            recordFilters.add(new RoomCityIdFilter(cityId, delegationCityId));
        }
    }

    private void addMaxRoomPriorityRecordFilter(List<RecordFilter> recordFilters, Employee employee) {
        int priority = employee.getPriority();
        recordFilters.add(RoomPriorityLowerThanFilter.ofPriority(priority));
    }
}
