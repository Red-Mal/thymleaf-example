# Person Registry - Spring Boot + Thymeleaf + Tailwind CSS

A modern web application for managing person records with passport information, built with Spring Boot, Thymeleaf, and Tailwind CSS.

## 🚀 Features

- **Person Management**: CRUD operations for person records
- **Passport Information**: Store and manage passport IDs and images
- **Status Management**: Track demande status (Pending, Approved, Rejected, In Review)
- **Search Functionality**: Search by name or passport ID
- **Image Upload**: Upload and view passport images
- **Modern UI**: Beautiful interface built with Tailwind CSS
- **Responsive Design**: Works on all devices

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.3, Spring Data JPA, H2 Database
- **Frontend**: Thymeleaf, Tailwind CSS, JavaScript
- **Build Tools**: Maven, npm
- **Database**: H2 (in-memory)

## 📦 Installation & Setup

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher

### 1. Clone and Setup
```bash
git clone <repository-url>
cd thymleaf-example
```

### 2. Install Frontend Dependencies
```bash
npm install
```

### 3. Build Tailwind CSS
```bash
# Development (with watch mode)
npm run build

# Production (minified)
npm run build:prod
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will be available at: http://localhost:8080

## 🎨 Tailwind CSS Setup

This project uses Tailwind CSS with a custom configuration optimized for Thymeleaf templates.

### Configuration Files
- `tailwind.config.js` - Tailwind configuration with shadcn/ui compatibility
- `postcss.config.js` - PostCSS configuration
- `src/main/resources/static/css/tailwind.css` - Custom Tailwind styles
- `src/main/resources/static/css/globals.css` - Global styles with CSS variables

### Custom Components
The project includes custom Tailwind components for:
- Buttons (primary, secondary, success, warning)
- Status badges
- Tables
- Modals
- Form inputs
- Image placeholders

### Development Workflow
1. Make changes to HTML templates
2. Run `npm run build` to watch for changes
3. Refresh browser to see updates

## 📋 Usage

### Adding Sample Data
1. Visit http://localhost:8080
2. Click "Add Sample Data" to populate the table

### Managing Records
- **Search**: Use the search form to find people by name or passport ID
- **Update Status**: Use the dropdown in the Actions column to change status
- **Upload Images**: Click "Upload" to add passport images
- **View Images**: Click "View" or the image thumbnail to see full-size images

### Features
- **Search**: Real-time search with result count
- **Status Management**: Four status types with color-coded badges
- **Image Handling**: Upload, preview, and download passport images
- **Responsive**: Works on desktop, tablet, and mobile

## 🗄️ Database

The application uses H2 in-memory database with the following schema:

```sql
CREATE TABLE persons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    passport_id VARCHAR(255) NOT NULL UNIQUE,
    passport_image VARCHAR(255),
    demande_status ENUM('PENDING', 'APPROVED', 'REJECTED', 'IN_REVIEW') NOT NULL
);
```

### Database Console
Access H2 console at: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## 🎯 API Endpoints

- `GET /` - Redirects to persons list
- `GET /persons` - Display all persons
- `GET /persons/search` - Search persons
- `POST /persons/update-status` - Update person status
- `POST /persons/upload-image` - Upload passport image
- `GET /persons/add-sample-data` - Add sample data

## 🎨 Customization

### Adding New Styles
1. Edit `src/main/resources/static/css/tailwind.css`
2. Add custom components using `@layer components`
3. Run `npm run build:prod` to rebuild CSS

### Modifying Colors
1. Update CSS variables in `src/main/resources/static/css/globals.css`
2. Modify the `:root` selector for light mode
3. Modify the `.dark` selector for dark mode

### Adding New Components
1. Create new component classes in `tailwind.css`
2. Use `@apply` directive to compose Tailwind utilities
3. Rebuild CSS and use in templates

## 🚀 Deployment

### Production Build
```bash
# Build frontend assets
npm run build:prod

# Build Spring Boot application
mvn clean package

# Run JAR file
java -jar target/thymleaf-example-0.0.1-SNAPSHOT.jar
```

### Environment Variables
- `SPRING_PROFILES_ACTIVE` - Set to `prod` for production
- `SPRING_DATASOURCE_URL` - Database connection string
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For issues and questions:
1. Check the documentation
2. Review existing issues
3. Create a new issue with detailed information 