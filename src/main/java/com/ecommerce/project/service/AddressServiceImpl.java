package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addresses = user.getAddresses();

        addresses.add(address);
        user.setAddresses(addresses);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream().map(a -> modelMapper.map(a, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO getAddressByAddressId(Long addressId) {

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

//        Address address = addressRepository.findByAddressId(addressId);

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {

        List<AddressDTO> addressesDTOS = user.getAddresses().stream().map(
                address -> modelMapper.map(address, AddressDTO.class)
        ).toList();

        return addressesDTOS;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO address) {

        Address addressDb = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

//        Address addressDb = addressRepository.findByAddressId(addressId);

        addressDb.setCity(address.getCity());
        addressDb.setState(address.getState());
        addressDb.setPincode(address.getPincode());
        addressDb.setStreet(address.getStreet());
        addressDb.setBuildingName(address.getBuildingName());
        addressDb.setCountry(address.getCountry());

        Address savedAddress = addressRepository.save(addressDb);

        User user = addressDb.getUser();

        List<Address> userAddresses = user.getAddresses();

        user.getAddresses().removeIf(ad -> ad.getAddressId().equals(addressId));
        user.getAddresses().add(savedAddress);

        userRepository.save(user);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
