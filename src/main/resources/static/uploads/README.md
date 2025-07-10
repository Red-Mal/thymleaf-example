# Uploads Directory

This directory stores uploaded passport images.

## Features:
- Images are automatically saved here when uploaded through the web interface
- Each image gets a unique filename to prevent conflicts
- Images are accessible via `/uploads/filename` URL

## File Naming Convention:
- Format: `passport_{personId}_{uuid}.{extension}`
- Example: `passport_1_a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg`

## Security:
- Only image files are accepted
- Files are validated before saving
- Unique filenames prevent overwriting 