package com.yourpackage.controller;

import com.yourpackage.model.FileRecord;
import com.yourpackage.model.OpsFile;
import com.yourpackage.model.ComplianceFile;
import com.yourpackage.repository.FileRecordRepository;
import com.yourpackage.repository.OpsRepository;
import com.yourpackage.repository.ComplianceRepository;
import com.yourpackage.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileRecordController {

    @Autowired
    private FileRecordRepository fileRecordRepository;

    @Autowired
    private OpsRepository opsRepository;

    @Autowired
    private ComplianceRepository complianceRepository;

    @Autowired
    private NotificationService notificationService;

    // ✅ Fetch all files from the common table
    @GetMapping
    public List<FileRecord> getAllFiles() {
        return fileRecordRepository.findAll();
    }

    // ✅ Add a new file record (uploaded by Ops)
    @PostMapping
    public ResponseEntity<String> addFile(@RequestBody FileRecord file) {
        try {
            file.setUploadedDate(LocalDateTime.now());
            file.setTransferred(false);
            fileRecordRepository.save(file);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving file: " + e.getMessage());
        }
    }

    // ✅ Transfer a file to respective table (OPS / COMPLIANCE)
    @PostMapping("/transfer/{id}")
    public ResponseEntity<String> transferFile(@PathVariable Long id) {
        FileRecord file = fileRecordRepository.findById(id).orElse(null);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            String fileName = file.getFileName().toLowerCase();
            String groupName = "";
            String message = "";

            // --- Determine target based on file name ---
            if (fileName.contains("ops")) {
                OpsFile opsFile = new OpsFile();
                opsFile.setFileName(file.getFileName());
                opsFile.setDestinationFolder(file.getDestinationFolder());
                opsFile.setUploadDate(LocalDateTime.now());
                opsRepository.save(opsFile);

                groupName = "Ops_Reports";
                message = "New OPS file uploaded: " + file.getFileName();
            } 
            else if (fileName.contains("compliance")) {
                ComplianceFile complianceFile = new ComplianceFile();
                complianceFile.setFileName(file.getFileName());
                complianceFile.setDestinationFolder(file.getDestinationFolder());
                complianceFile.setUploadDate(LocalDateTime.now());
                complianceRepository.save(complianceFile);

                groupName = "Compliance_Data";
                message = "New COMPLIANCE file uploaded: " + file.getFileName();
            } 
            else {
                return ResponseEntity.badRequest()
                        .body("File name does not match OPS or COMPLIANCE category.");
            }

            // ✅ Notify all subscribers of that group
            notificationService.notifySubscribers(groupName, message);

            // ✅ Mark file as transferred in common table
            file.setTransferred(true);
            fileRecordRepository.save(file);

            return ResponseEntity.ok("File transferred and notification sent successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during file transfer: " + e.getMessage());
        }
    }

    // ✅ Download a specific file (if needed)
    @GetMapping("/download/{id}")
    public ResponseEntity<FileRecord> downloadFile(@PathVariable Long id) {
        FileRecord file = fileRecordRepository.findById(id).orElse(null);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(file);
    }

    // ✅ Delete a file (optional admin use)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        try {
            fileRecordRepository.deleteById(id);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting file: " + e.getMessage());
        }
    }
}
