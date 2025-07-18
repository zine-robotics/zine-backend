package com.dev.zine.api.controllers.roomMembers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.roomMembers.MemberRoleUpdate;
import com.dev.zine.api.model.roomMembers.MembersList;
import com.dev.zine.api.model.roomMembers.MembersResponse;
import com.dev.zine.api.model.roomMembers.MembersRoomsList;
import com.dev.zine.api.model.roomMembers.RemoveMembersList;
import com.dev.zine.api.model.roomMembers.RemoveMembersRoomsList;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.api.model.user.AssignResponseMultipleRooms;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.RoomMemberNotFound;
import com.dev.zine.service.RoomMembersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/members")
public class RoomMembersController {
    @Autowired
    private RoomMembersService roomMembersService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody MembersList addMembersBody) {
        System.out.println("addMembersBody");
        System.out.println(addMembersBody);
        try {
            AssignResponse res = roomMembersService.addMembers(addMembersBody);
            return ResponseEntity.ok().body(res);
        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add-to-rooms")
    public ResponseEntity<?> addRooms(@Valid @RequestBody MembersRoomsList addMembersBody) {
        // System.out.println("addMembersBody");
        // System.out.println(addMembersBody);
        try {
            AssignResponseMultipleRooms res = roomMembersService.addMembersToRooms(addMembersBody);
            return ResponseEntity.ok().body(res);
        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<?> remove(@Valid @RequestBody RemoveMembersList removeMembersBody) {
        System.out.println(removeMembersBody);
        try {
            String message = roomMembersService.removeMembers(removeMembersBody);
            return ResponseEntity.ok(message);
        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove-from-rooms")
    public ResponseEntity<?> removeMemberFromRooms(@Valid @RequestBody RemoveMembersRoomsList body) {
        try {
            roomMembersService.removeMembersFromRooms(body);;
            return ResponseEntity.ok().build();
        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRoomMembers(@RequestParam Long roomId) {
        System.out.println(roomId);
        try {

            List<MembersResponse> res = roomMembersService.getMemebers(roomId);

            return ResponseEntity.ok(res);

        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody MemberRoleUpdate update) throws RoomDoesNotExist, RoomMemberNotFound{
        try{
            roomMembersService.updateMemberRole(update);
            return ResponseEntity.ok().build();
        } catch(RoomDoesNotExist e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(RoomMemberNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
