@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion

:: --- 配置区域 ---
set "MAIN_BRANCH=main"
set "BUILD_COMMAND=pnpm docs:build"
set "DIST_FOLDER=src\.vuepress\dist"
set "USERNAME=Reallylearning"
set "REPO=Reallylearning.github.io"
set "DOMAIN=www.reallylearning.icu"
:: --- 配置结束 ---

echo ✅ 开始执行部署脚本...

:: 1. 检查当前分支
for /f "tokens=*" %%i in ('git rev-parse --abbrev-ref HEAD') do set "current_branch=%%i"
if not "%current_branch%"=="%MAIN_BRANCH%" (
    echo ❌ 错误：当前分支不是 '%MAIN_BRANCH%'，而是 '%current_branch%'。
    echo     请先切换到主分支再执行，例如：git checkout %MAIN_BRANCH%
    goto :end
)
echo ✅ 当前分支检查通过: %MAIN_BRANCH%

:: (已删除) 2. 拉取最新代码
:: echo ⏳ 正在拉取最新代码...
:: git pull origin %MAIN_BRANCH%
:: if errorlevel 1 (
::     echo ❌ 拉取代码失败，请检查网络或Git配置。
::     pause
::     exit /b
:: )
:: echo ✅ 代码拉取成功

:: 3. 执行构建命令
echo ⏳ 正在执行构建...
call %BUILD_COMMAND%
if errorlevel 1 (
    echo ❌ 构建失败！命令 '%BUILD_COMMAND%' 执行出错。
    pause
    exit /b
)
echo ✅ 项目构建成功

:: 4. 进入构建产物目录
cd %DIST_FOLDER%
if errorlevel 1 (
    echo ❌ 错误：无法进入构建目录 '%DIST_FOLDER%'。
    pause
    exit /b
)

:: 5. 创建CNAME文件 (如果需要)
if not "%DOMAIN%"=="" (
    echo %DOMAIN% > CNAME
    echo ✅ 创建了CNAME文件，域名: %DOMAIN%
)

:: 6. 初始化Git并提交
echo ⏳ 正在准备部署到GitHub Pages...
git init
git add -A
git commit -m "deploy: update site content"

:: 7. 强制推送到远程仓库
if "%REPO%"=="%USERNAME%.github.io" (
    git push -f git@github.com:%USERNAME%/%REPO%.git master
) else (
    git push -f git@github.com:%USERNAME%/%REPO%.git master:gh-pages
)
if errorlevel 1 (
    echo ❌ 推送失败，请检查SSH密钥或仓库权限。
    pause
    exit /b
)
echo ✅ 成功部署到GitHub Pages!

:: 8. 返回原目录
cd ..\..\..\
echo ✅ 部署流程执行完毕。

:end
endlocal
pause