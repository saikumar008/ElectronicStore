package com.project.Electronic_Store.service.serviceimpl;

import com.project.Electronic_Store.ExceptionHandling.ResourceNotFoundException;
import com.project.Electronic_Store.dto.UserDto;
import com.project.Electronic_Store.dto.pageableResponse;
import com.project.Electronic_Store.entity.User;
import com.project.Electronic_Store.helper.Helper;
import com.project.Electronic_Store.repository.UserRepository;
import com.project.Electronic_Store.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;
    public UserServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    // Creating user
    @Override
    public UserDto createUser(UserDto userDto) {

//        String userId = UUID.randomUUID().toString();
//        userDto.setUserId(userId);
//        //encoding password
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        // dto->entity
//        User user = dtoToEntity(userDto);
//        //fetch role of normal and set it to user
//        Role role = roleRepository.findById(normalRoleId).get();
//        user.getRoles().add(role);
//        User savedUser = userRepository.save(user);
//        //entity -> dto
//        UserDto newDto = entityToDto(savedUser);
//        return newDto;
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = DtoToEntity(userDto);

//        user.setUserName(userDto.getUserName());
//        user.setEmailId(userDto.getEmailId());
//        user.setGender(userDto.getGender());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        user.setUserImage(userDto.getUserImage());
//        user.setAbout(userDto.getAbout());
        User saveuser = userRepo.save(user);



        UserDto userDto1 = entityToDto(saveuser);


//        userDto1.setUserName(saveuser.getUsername());
//        userDto1.setEmailId(saveuser.getEmailId());
//        userDto1.setGender(saveuser.getGender());
//        userDto1.setPassword(saveuser.getPassword());
//        userDto1.setUserImage(saveuser.getUserImage());
//        userDto1.setAbout(saveuser.getAbout());
        return userDto1;
    }

    @Override
    public pageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc"))? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

         Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

         Page<User> page = userRepo.findAll(pageable);

        pageableResponse<UserDto> response= Helper.getPageableResponse(page, UserDto.class);

        return response;
    }


    @Override
    public void deleteUserById(String id) {
        UUID uuid=UUID.fromString(id);
        User user = userRepo.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepo.deleteById(user.getId());
    }

    @Override
    public UserDto getSingleUserById(UUID id) {
        User user = userRepo.findById(UUID.fromString(String.valueOf(id))).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityToDto(user);
    }


    @Override
    public UserDto updateUser(UserDto userDto, String id){
        UUID uuid=UUID.fromString(id);
        User user = userRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(userDto.getUserName());
        user.setGender(userDto.getGender());
        user.setUserImage(userDto.getUserImage());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());

        User updatedUser = userRepo.save(user);

        UserDto userRes = entityToDto(updatedUser);

        return userRes;
    }

    @Override
    public UserDto getUserByEmail(String emailId) {
//        User users = userRepo.findByEmailId(emailId).orElseThrow(() -> new RuntimeException("User not found with this email id"));
//        return entityToRes(users);
        Optional<User> optionalUser = userRepo.findByEmailId(emailId);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with this email id");
        }
        User user = optionalUser.get();
        return entityToDto(user);
    }

//    @Override
//    public List<UserResponse> searchUser(String keyWord) {
//        List<User> users = userRepo.findByKeyWord(keyWord);
//        List<UserResponse> userRepo = users.stream().map(user -> entityToRes(user)).collect(Collectors.toList());
//
//        return userRepo;
//    }



    //Request to entity
    private User DtoToEntity(UserDto userDto){
        User user = new User();

//      user.setUserName(userDto.getUserName());
//        user.setEmailId(userDto.getEmailId());
//        user.setGender(userDto.getGender());
//        user.setPassword(userDto.getPassword());
//        user.setUserImage(userDto.getUserImage());
//        user.setAbout(userDto.getAbout());

        return modelMapper.map(userDto, User.class);
    }

    //Entity to response
    private UserDto entityToDto(User saveuser){

//        UserDto userDto = new UserDto();
//
//        userDto.setUserName(saveuser.getUsername());
//        userDto.setEmailId(saveuser.getEmailId());
//        userDto.setGender(saveuser.getGender());
//        userDto.setPassword(saveuser.getPassword());
//        userDto.setUserImage(saveuser.getUserImage());
//        userDto.setAbout(saveuser.getAbout());
        return modelMapper.map(saveuser, UserDto.class);

    }

}
