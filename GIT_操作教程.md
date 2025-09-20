# Git 常用操作教程 - Smart Boot3 项目

本文档以 Smart Boot3 微服务框架项目为例，详细介绍 Git 的常用操作。

## 目录
- [基础配置](#基础配置)
- [仓库初始化](#仓库初始化)
- [文件操作](#文件操作)
- [提交管理](#提交管理)
- [分支管理](#分支管理)
- [标签管理](#标签管理)
- [远程仓库操作](#远程仓库操作)
- [撤销操作](#撤销操作)
- [查看历史](#查看历史)
- [协作开发](#协作开发)

## 基础配置

### 设置用户信息
```bash
# 设置全局用户名和邮箱
git config --global user.name "您的姓名"
git config --global user.email "您的邮箱@example.com"

# 查看当前配置
git config --list
```

### 设置默认分支名
```bash
# 设置默认分支为 main
git config --global init.defaultBranch main
```

## 仓库初始化

### 初始化新仓库
```bash
# 在当前目录初始化Git仓库
git init

# 查看仓库状态
git status
```

### 克隆现有仓库
```bash
# 克隆远程仓库
git clone https://github.com/用户名/smart-boot3.git

# 克隆到指定目录
git clone https://github.com/用户名/smart-boot3.git my-project
```

## 文件操作

### 添加文件到暂存区
```bash
# 添加单个文件
git add README.md

# 添加多个文件
git add file1.java file2.java

# 添加所有文件
git add .

# 添加所有Java文件
git add *.java

# 添加所有修改的文件
git add -u
```

### 查看文件状态
```bash
# 查看工作区状态
git status

# 查看文件差异
git diff

# 查看暂存区差异
git diff --cached

# 查看具体文件的差异
git diff smart-system/src/main/java/com/smart/system/controller/AuthController.java
```

### 移除文件
```bash
# 从Git中删除文件（保留本地文件）
git rm --cached file.txt

# 从Git和本地都删除文件
git rm file.txt

# 删除目录
git rm -r directory/
```

## 提交管理

### 创建提交
```bash
# 创建提交
git commit -m "feat: 添加用户认证功能"

# 添加文件并提交（跳过暂存区）
git commit -am "fix: 修复登录bug"

# 修改最后一次提交
git commit --amend -m "feat: 完善用户认证功能"
```

### 提交信息规范
推荐使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```bash
# 功能新增
git commit -m "feat: 添加JWT认证功能"

# 修复bug
git commit -m "fix: 修复登录验证逻辑"

# 文档更新
git commit -m "docs: 更新API文档"

# 代码重构
git commit -m "refactor: 重构安全配置类"

# 性能优化
git commit -m "perf: 优化数据库查询性能"

# 测试相关
git commit -m "test: 添加单元测试用例"

# 构建相关
git commit -m "build: 更新Maven依赖版本"
```

## 分支管理

### 查看分支
```bash
# 查看本地分支
git branch

# 查看所有分支（包括远程）
git branch -a

# 查看分支详细信息
git branch -v
```

### 创建和切换分支
```bash
# 创建新分支
git branch feature/user-management

# 切换到分支
git checkout feature/user-management

# 创建并切换到新分支
git checkout -b feature/user-management

# 使用新语法创建并切换分支
git switch -c feature/user-management
```

### 分支操作示例
```bash
# 1. 创建功能分支
git checkout -b feature/add-redis-cache

# 2. 在分支上开发
echo "添加Redis缓存配置" >> smart-common/smart-common-redis/README.md
git add .
git commit -m "feat: 添加Redis缓存配置"

# 3. 切换回主分支
git checkout main

# 4. 合并分支
git merge feature/add-redis-cache

# 5. 删除已合并的分支
git branch -d feature/add-redis-cache
```

### 分支合并
```bash
# 合并分支到当前分支
git merge feature/user-management

# 使用快进合并
git merge --ff-only feature/user-management

# 创建合并提交
git merge --no-ff feature/user-management

# 解决合并冲突后继续
git add .
git commit
```

## 标签管理

### 创建标签
```bash
# 创建轻量标签
git tag v1.0.0

# 创建带注释的标签
git tag -a v1.0.0 -m "Smart Boot3 第一个正式版本"

# 为特定提交创建标签
git tag -a v1.0.0 9fceb02 -m "Smart Boot3 第一个正式版本"
```

### 标签操作
```bash
# 查看所有标签
git tag

# 查看标签详细信息
git show v1.0.0

# 推送标签到远程
git push origin v1.0.0

# 推送所有标签
git push origin --tags

# 删除本地标签
git tag -d v1.0.0

# 删除远程标签
git push origin --delete v1.0.0
```

### 版本发布示例
```bash
# 1. 确保代码已提交
git status

# 2. 创建版本标签
git tag -a v1.0.0 -m "Smart Boot3 v1.0.0 - 初始版本发布"

# 3. 推送标签
git push origin v1.0.0

# 4. 在GitHub上创建Release
# 访问 https://github.com/用户名/smart-boot3/releases
# 点击 "Create a new release"
# 选择标签 v1.0.0
# 填写发布说明
```

## 远程仓库操作

### 添加远程仓库
```bash
# 添加远程仓库
git remote add origin https://github.com/用户名/smart-boot3.git

# 查看远程仓库
git remote -v

# 修改远程仓库URL
git remote set-url origin https://github.com/新用户名/smart-boot3.git
```

### 推送和拉取
```bash
# 推送到远程仓库
git push origin main

# 首次推送并设置上游分支
git push -u origin main

# 拉取远程更新
git pull origin main

# 获取远程更新但不合并
git fetch origin

# 推送所有分支
git push --all origin

# 推送标签
git push --tags origin
```

### 克隆和协作
```bash
# 克隆仓库
git clone https://github.com/用户名/smart-boot3.git

# Fork后克隆自己的仓库
git clone https://github.com/您的用户名/smart-boot3.git

# 添加上游仓库
git remote add upstream https://github.com/原用户名/smart-boot3.git

# 同步上游更新
git fetch upstream
git checkout main
git merge upstream/main
```

## 撤销操作

### 撤销工作区修改
```bash
# 撤销单个文件的修改
git checkout -- smart-system/src/main/java/com/smart/system/controller/AuthController.java

# 撤销所有修改
git checkout -- .

# 使用新语法撤销修改
git restore smart-system/src/main/java/com/smart/system/controller/AuthController.java
```

### 撤销暂存区
```bash
# 从暂存区移除文件
git reset HEAD smart-system/src/main/java/com/smart/system/controller/AuthController.java

# 使用新语法
git restore --staged smart-system/src/main/java/com/smart/system/controller/AuthController.java
```

### 撤销提交
```bash
# 撤销最后一次提交（保留修改）
git reset --soft HEAD~1

# 撤销最后一次提交（不保留修改）
git reset --hard HEAD~1

# 撤销到指定提交
git reset --hard 9fceb02

# 修改最后一次提交
git commit --amend -m "新的提交信息"
```

### 撤销合并
```bash
# 撤销合并
git reset --hard HEAD~1

# 或者使用
git reset --hard ORIG_HEAD
```

## 查看历史

### 查看提交历史
```bash
# 查看提交历史
git log

# 查看简洁历史
git log --oneline

# 查看图形化历史
git log --graph --oneline --all

# 查看最近5次提交
git log -5

# 查看特定文件的提交历史
git log -- smart-system/src/main/java/com/smart/system/controller/AuthController.java

# 查看提交统计
git log --stat
```

### 查看差异
```bash
# 查看工作区与暂存区的差异
git diff

# 查看暂存区与最后一次提交的差异
git diff --cached

# 查看两次提交之间的差异
git diff 9fceb02..HEAD

# 查看特定文件的差异
git diff HEAD~1 HEAD -- smart-system/src/main/java/com/smart/system/controller/AuthController.java
```

### 查看文件历史
```bash
# 查看文件的修改历史
git log -p smart-system/src/main/java/com/smart/system/controller/AuthController.java

# 查看文件的每一行修改历史
git blame smart-system/src/main/java/com/smart/system/controller/AuthController.java
```

## 协作开发

### Pull Request 工作流
```bash
# 1. Fork 仓库到自己的GitHub账户

# 2. 克隆自己的仓库
git clone https://github.com/您的用户名/smart-boot3.git

# 3. 添加上游仓库
git remote add upstream https://github.com/原用户名/smart-boot3.git

# 4. 创建功能分支
git checkout -b feature/add-new-module

# 5. 开发并提交
git add .
git commit -m "feat: 添加新模块"

# 6. 推送到自己的仓库
git push origin feature/add-new-module

# 7. 在GitHub上创建Pull Request

# 8. 同步上游更新
git fetch upstream
git checkout main
git merge upstream/main
```

### 代码审查
```bash
# 查看Pull Request
git fetch origin pull/123/head:pr-123
git checkout pr-123

# 测试Pull Request
git checkout main
git merge pr-123
```

## 常用别名设置

在 `~/.gitconfig` 中添加以下别名：

```bash
[alias]
    st = status
    co = checkout
    br = branch
    ci = commit
    unstage = reset HEAD --
    last = log -1 HEAD
    visual = !gitk
    lg = log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit
```

## 最佳实践

### 1. 提交频率
- 频繁提交，每次提交解决一个问题
- 提交前确保代码可以编译通过
- 使用有意义的提交信息

### 2. 分支策略
- 主分支（main）保持稳定
- 功能开发使用独立分支
- 及时删除已合并的分支

### 3. 代码审查
- 重要功能必须经过代码审查
- 使用Pull Request进行协作
- 保持代码风格一致

### 4. 版本管理
- 使用语义化版本号
- 重要版本创建标签
- 维护详细的变更日志

## 故障排除

### 常见问题解决

1. **合并冲突**
```bash
# 查看冲突文件
git status

# 手动解决冲突后
git add .
git commit
```

2. **提交到错误分支**
```bash
# 创建新分支保存提交
git branch feature/save-work

# 重置当前分支
git reset --hard HEAD~1

# 切换到正确分支
git checkout correct-branch

# 应用保存的提交
git cherry-pick feature/save-work
```

3. **误删文件**
```bash
# 恢复文件
git checkout HEAD -- deleted-file.java
```

4. **修改远程仓库URL**
```bash
# 查看当前远程仓库
git remote -v

# 修改URL
git remote set-url origin https://github.com/新用户名/smart-boot3.git
```

## 总结

Git是一个强大的版本控制工具，掌握这些常用操作能够大大提高开发效率。在实际使用中，建议：

1. 经常使用 `git status` 查看状态
2. 提交前使用 `git diff` 检查修改
3. 使用分支进行功能开发
4. 保持提交信息的规范性
5. 定期同步远程仓库

通过本教程的学习，您应该能够熟练使用Git进行日常的版本控制操作。如有疑问，可以参考Git官方文档或寻求帮助。
