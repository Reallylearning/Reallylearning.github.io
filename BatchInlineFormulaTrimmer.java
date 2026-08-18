package com.xl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatchInlineFormulaTrimmer {

    // 配置参数（根据实际路径修改）
    private static final String SOURCE_FOLDER_PATH = "D:\\Projects\\IdeaProjects\\leetcode\\src\\main\\resources";
    private static final String OUTPUT_FOLDER_PATH = "D:\\Projects\\IdeaProjects\\leetcode\\src\\main\\resources";

    // 正则表达式（核心匹配规则）
    // 匹配带有引用符号>的块级公式 align* 环境（支持多行带>的情况）
    private static final Pattern ALIGN_BLOCK_WITH_QUOTE_PATTERN = Pattern.compile(
            "(?m)(^|\\n)\\s*>\\s*(\\\\begin\\{align\\*\\})([\\s\\S]*?)\\s*>\\s*(\\\\end\\{align\\*\\})(\\s*|$)");
    
    // 匹配没有被 $$ 包裹且不含引用符号的块级公式 align* 环境
    private static final Pattern ALIGN_BLOCK_RAW_PATTERN = Pattern.compile(
            "([^$]|^)(\\\\begin\\{align\\*\\}[\\s\\S]*?\\\\end\\{align\\*\\})([^$]|$)");
    
    // 匹配行内公式：$...$（含内容前后空格）
    private static final Pattern INLINE_FORMULA_PATTERN = Pattern.compile("\\$(\\s*)([\\s\\S]*?)(\\s*)\\$");
    
    // 匹配通用加粗格式：**XXX**（XXX以中英文冒号结尾）
    private static final Pattern BOLD_COLON_PATTERN = Pattern.compile("\\*\\*(.*?)([：:])(\\s*)\\*\\*");


    public static void main(String[] args) {
        try {
            Path sourceFolder = Paths.get(SOURCE_FOLDER_PATH);
            Path outputFolder = Paths.get(OUTPUT_FOLDER_PATH);

            if (!Files.exists(sourceFolder) || !Files.isDirectory(sourceFolder)) {
                throw new IllegalArgumentException("❌ 原文件夹不存在或不是目录：" + sourceFolder.toAbsolutePath());
            }

            Files.createDirectories(outputFolder);
            System.out.println("✅ 输出文件夹已就绪：" + outputFolder.toAbsolutePath());

            Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attrs) throws IOException {
                    if (sourceFile.getFileName().toString().toLowerCase().endsWith(".md")) {
                        processSingleMdFile(sourceFile, outputFolder);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.err.println("❌ 访问文件失败：" + file.toAbsolutePath() + "，原因：" + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("\n🎉 所有 Markdown 文件处理完成！输出目录：" + outputFolder.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ 文件夹遍历出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * 处理单个 Markdown 文件：
     * 读取 → 修正加粗冒号 → 修正块级公式包裹 → 修剪行内公式 → 保存
     */
    private static void processSingleMdFile(Path sourceFile, Path outputRootFolder) throws IOException {
        try {
            Path relativePath = Paths.get(SOURCE_FOLDER_PATH).relativize(sourceFile);
            Path outputFile = outputRootFolder.resolve(relativePath);
            Files.createDirectories(outputFile.getParent());

            System.out.println("\n🔍 处理文件：" + sourceFile.toAbsolutePath());
            String content = Files.readString(sourceFile, StandardCharsets.UTF_8);

            // 步骤1：修正加粗冒号格式
            String contentAfterBoldFix = fixBoldColonFormat(content);

            // 步骤2：修正块级公式包裹（先处理带引用符号的，再处理普通的）
            String contentAfterQuoteBlockFix = fixQuotedAlignBlockWrapping(contentAfterBoldFix);
            String contentAfterBlockFix = fixRawAlignBlockWrapping(contentAfterQuoteBlockFix);

            // 步骤3：修剪行内公式
            String processedContent = trimInlineFormulaSpaces(contentAfterBlockFix);

            Files.writeString(outputFile, processedContent, StandardCharsets.UTF_8);
            System.out.println("✅ 处理完成，保存至：" + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ 处理文件失败：" + sourceFile.toAbsolutePath() + "，原因：" + e.getMessage());
            throw e;
        }
    }

    /**
     * 处理带有引用符号>的块级公式，移除>并添加正确的$$包裹
     */
    private static String fixQuotedAlignBlockWrapping(String content) {
        Matcher matcher = ALIGN_BLOCK_WITH_QUOTE_PATTERN.matcher(content);
        StringBuffer resultBuffer = new StringBuffer();

        while (matcher.find()) {
            // 获取公式块内容（group2是begin，group3是中间内容，group4是end）
            String alignStart = matcher.group(2);
            String alignContent = matcher.group(3);
            String alignEnd = matcher.group(4);
            
            // 移除内容中每行的引用符号>及前后空格
            alignContent = alignContent.replaceAll("(?m)^\\s*>\\s*", "");
            
            // 构建完整公式块并添加正确包裹
            String fixedBlock = "$" + alignStart + alignContent + alignEnd + "$";
            
            matcher.appendReplacement(resultBuffer, Matcher.quoteReplacement(fixedBlock));
        }

        matcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }

    /**
     * 处理普通块级公式（不含引用符号），添加正确的$$包裹
     */
    private static String fixRawAlignBlockWrapping(String content) {
        Matcher matcher = ALIGN_BLOCK_RAW_PATTERN.matcher(content);
        StringBuffer resultBuffer = new StringBuffer();

        while (matcher.find()) {
            String alignBlock = matcher.group(2);
            String fixedBlock = "$" + alignBlock + "$";
            matcher.appendReplacement(resultBuffer, Matcher.quoteReplacement(fixedBlock));
        }

        matcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }


    // 修正加粗冒号格式（保持不变）
    private static String fixBoldColonFormat(String content) {
        Matcher matcher = BOLD_COLON_PATTERN.matcher(content);
        StringBuffer resultBuffer = new StringBuffer();
        while (matcher.find()) {
            String coreText = matcher.group(1);
            String colon = matcher.group(2);
            String spacesAfterColon = matcher.group(3);
            String fixedBold = "**" + coreText + "**" + colon + spacesAfterColon;
            matcher.appendReplacement(resultBuffer, Matcher.quoteReplacement(fixedBold));
        }
        matcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }

    // 修剪行内公式空格（保持不变）
    private static String trimInlineFormulaSpaces(String content) {
        Matcher matcher = INLINE_FORMULA_PATTERN.matcher(content);
        StringBuffer resultBuffer = new StringBuffer();
        while (matcher.find()) {
            String coreFormula = matcher.group(2).trim();
            String fixedFormula = "$" + coreFormula + "$";
            matcher.appendReplacement(resultBuffer, Matcher.quoteReplacement(fixedFormula));
        }
        matcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }
}