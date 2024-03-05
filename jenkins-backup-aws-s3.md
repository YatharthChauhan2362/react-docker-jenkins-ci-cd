# Jenkins Backup on AWS S3

Taking a backup of Jenkins, storing it in an Amazon S3 bucket, deleting the entire Jenkins setup, and then restoring Jenkins using the backup from the S3 bucket.

## Backup and Restore Steps

### 1. Set up AWS CLI Configuration
```bash
sudo aws configure
```

### 2. Upload Backup to S3
```bash
aws s3 cp jenkins_backup.tar.gz s3://devops-training-yatharth/jenkins_backup/
```

### 3. Download Backup from S3 (Optional)
```bash
sudo aws s3 cp s3://devops-training-yatharth/jenkins_backup/jenkins_backup.tar.gz /backup
```

### 4. Backup Jenkins Configuration to S3
```bash
aws s3 backup
```

### 5. Delete Jenkins Setup
```bash
sudo rm -rf /var/lib/jenkins
```

### 6. Restore Jenkins from Backup
```bash
sudo tar -zxvf jenkins_backup.tar.gz -C /
```

## Resources
- S3 Bucket: [devops-training-yatharth](https://s3.console.aws.amazon.com/s3/buckets/devops-training-yatharth?region=us-east-1&bucketType=general&tab=objects)
- Backup File on AWS S3: [jenkins_backup.tar.gz](https://s3.console.aws.amazon.com/s3/object/devops-training-yatharth?region=us-east-1&bucketType=general&prefix=jenkins_backup/jenkins_backup.tar.gz)

### Author

#### - [Visit My Portfolio](https://yatharthchauhan.me)

#### - [LinkedIn: Yatharth Chauhan](https://www.linkedin.com/in/yatharth-chauhan-729674202/)

#### - [GitHub: Yatharth Chauhan](https://github.com/YatharthChauhan2362)