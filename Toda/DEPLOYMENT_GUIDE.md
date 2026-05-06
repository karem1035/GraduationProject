# Toda Coolify Deployment Guide

## 🚀 Super Easy Deployment

### What You Need
- Your Coolify VPS (already running)
- Your Git repository (already set up)

### Files Created ✅
- `Dockerfile` - Builds your Spring Boot app
- `docker-compose.yml` - Runs MySQL, Artemis, and your app together
- `.dockerignore` - Excludes unnecessary files from Docker build

---

## 📋 Deployment Steps (5 minutes total)

### Step 1: Push Changes to Git
```bash
git add .
git commit -m "Add Docker deployment files"
git push origin main
```

### Step 2: Go to Coolify Dashboard
1. Open your Coolify dashboard
2. Click **"New Application"** or **"New Project"**
3. Select **"Git Repository"** (GitHub/GitLab)
4. Choose your repository

### Step 3: Configure Application
1. **Build Type**: Select **"Docker Compose"**
2. **Docker Compose File**: Select `Toda/docker-compose.yml`
3. **Project Path**: Select the `Toda/` directory

### Step 4: Deploy
1. Click **"Deploy"** button
2. Wait for Coolify to build everything (first time takes ~5-10 minutes)
3. Done! 🎉

---

## 🌐 Access Your Application

After deployment, Coolify will show you:
- **Application URL**: Your live API (e.g., `http://your-domain.com` or `http://IP:8080`)
- **Database**: MySQL running on port 3306
- **Artemis Console**: Running on port 8161

---

## 📦 What Happens Automatically

Coolify will:
1. ✅ Build your Spring Boot app with Maven
2. ✅ Start MySQL database with your credentials
3. ✅ Start Artemis message broker
4. ✅ Connect all services together
5. ✅ Handle networking between containers
6. ✅ Store data in persistent volumes
7. ✅ Store uploaded files in persistent volume

---

## 🔧 Ports Exposed

- **8080** - Your Spring Boot API
- **3306** - MySQL Database
- **61616** - Artemis (AMQP port)
- **8161** - Artemis Web Console

---

## 💡 Important Notes

1. **First Start**: The app will take 2-3 minutes to fully start (waiting for MySQL)
2. **Database**: Uses `create-drop` mode, so tables are recreated on restart
3. **File Uploads**: Uploaded files are saved in a persistent volume (won't be lost on restart)
4. **Email**: Uses your existing Gmail credentials

---

## 🐛 Troubleshooting

### App won't start?
- Check Coolify logs for each container
- Make sure MySQL is healthy before app starts (automatic with healthcheck)

### Can't access database?
- Ensure MySQL container is running
- Check if network `toda-network` exists

### Files not uploading?
- Check if uploads-data volume is mounted
- Verify permissions in the container

---

## 🔄 Updating Your App

After making code changes:
1. Commit and push to Git
2. Go to Coolify
3. Click **"Redeploy"**
4. That's it!

---

## 📝 Next Steps (Optional)

For production, you might want to:
1. Add a custom domain in Coolify
2. Enable SSL/HTTPS (Coolify does this automatically)
3. Change `create-drop` to `update` in `application.properties` to preserve data
4. Set up automated backups

---

**That's it! Your app is now deployed on Coolify! 🚀**