**[原文链接](https://medium.freecodecamp.org/how-to-use-git-efficiently-54320a236369)**

![](https://ws1.sinaimg.cn/large/0069RVTdly1fuz415uvavj318g0tmh0f.jpg)

> 代码昨天还是运行好好的今天就不行了。

> 代码被删了。

> 突然出现了一个奇怪的 bug，但是没人知道怎么回事。


如果你出现过上面的任何一种情况，那本篇文章就是为你准备的。

除了知道 `git add`, `git commit` , `git push` 之外，Git 中还需要其他重要的技术需要掌握。长远来看对我们是有帮助的。这里我将向你展示 Git 的最佳实践。


<!--more-->

# Git 工作流

当有多个开发者同时涉及到一个项目时那么就非常有必要正确使用 Git 工作流。

这里我将介绍一种工作流，它在一个多人大型项目中将非常有用。

![](https://ws1.sinaimg.cn/large/0069RVTdly1fuz4imimuuj313111zq6q.jpg)


# 前言

突然有一天，你成为了一个项目的技术 Leader 并计划做出下一个 Facebook。在这个项目中你有三个开发人员。

1. Alice：一个开发小白。
2. Bob：拥有一年工作经验，了解基本开发。
3. John：三年开发经验，熟练开发技能。
4. 你：该项目的技术负责人。

# Git 开发流程

## Master 分支

1. Master 分支应该始终和生产环境保持一致。
2. 由于 master 和生产代码是一致的，所以没有人包括技术负责人能在 master 上直接开发。
3. 真正的开发代码应当写在其他分支上。

## Release(发布) 分支

1. 当项目开始时，第一件事情就是创建发布分支。发布分支是基于 master 分支创建而来。
2. 所有与本项目相关的代码都在发布分支中，这个分支也是一个以 `release/` 开头的普通分支。
3. 比如这次的发布分支名为 `release/fb`。
4. 可能有多个项目都基于同一份代码运行，因此对于每一个项目来说都需要创建一个独立的发布分支。假设现在还有一个项目正在并行运行，那就得为这个项目创建一个单独的发布分支比如 `release/messenger`。
5. 需要单独的发布分支的原因是：多个并行项目是基于同一份代码运行的，但是项目之间不能有冲突。

## Feature(功能分支) branch

1. 对于应用中的每一个功能都应该创建一个独立的功能分支，这会确保这些功能能被单独构建。
2. 功能分支也和其他分支一样，只是以 `feature/` 开头。
3. 现在作为技术 Leader，你要求 Alice 去做 Facebook 的登录页面。因此他创建了一个新的功能分支。把他命名为 `feature/login`。Alice 将会在这个分支上编写所有的登录代码。
4. 这个功能分支通常是基于 Release(发布) 分支 创建而来。
5. Bob 的任务为创建添加好友页面，因此他创建了一个名为 `feature/friendrequest` 的功能分支。
6. John 则被安排构建消息流，因此创建了一个 `feature/newsfeed` 的功能分支。
7. 所有的开发人员都在自己的分支上进行开发，目前为止都很正常。
8. 现在当 Alice 完成了他的登录开发，他需要将他的功能分支 `feature/login` 发送给 Release(发布) 分支。这个过程是通过发起一个 `pull request` 完成的。


## Pull request

首先 `pull request` 不能和 `git pull` 搞混了。

开发人员不能直接向 Release(发布) 分支推送代码，技术 Leader 需要在功能分支合并到 Release(发布) 分支之前做好代码审查。这也是通过 `pull request` 完成的。

Alice 能够按照如下 GitHub 方式提交 `pull request`。

![](https://ws1.sinaimg.cn/large/0069RVTdgy1fv03386jcoj30ig05swet.jpg)

在分支名字的旁边有一个 “New pull request” 按钮，点击之后将会显示如下界面：

![](https://ws4.sinaimg.cn/large/0069RVTdgy1fv03etb1afj30no078gmn.jpg)

- 比较分支是 Alice 的功能分支 `feature/login`。
- base 分支则应该是发布分支 `release/fb`。

点击之后 Alice 需要为这个 `pull request` 输入名称和描述，最后再点击 “Create Pull Request” 按钮。

同时 Alice 需要为这个 `pull request` 指定一个 reviewer。作为技术 Leader 的你被选为本次 `pull request` 的 reviewer。

你完成代码审查之后就需要把这个功能分支合并到 Release(发布) 分支。

现在你已经把 `feature/login` 分支合并到 `release/fb`，并且 Alice 非常高兴他的代码被合并了。

## 代码冲突 😠

1. Bob 完成了他的编码工作，同时向 `release/fb` 分支发起了一个 `pull request`。
2. 因为发布分支已经合并了登录的代码，这时代码冲突发生了。解决冲突和合并代码是 reviewer 的责任。在这样的情况下，作为技术 Leader 就需要解决冲突和合并代码了。
3. 现在 John 也已经完成了他的开发，同时也想把代码合并到发布分支。但 John 非常擅长于解决代码冲突。他将 `release/fb` 上最新的代码合并到他自己的功能分支 `feature/newsfeed` （通过 git pull 或 git merge 命令）。同时他解决了所有存在的冲突，现在 `feature/newsfeed` 已经有了所有发布分支 `release/fb` 的代码。
4. 最后 John 创建了一个 `pull request`，由于 John 已经解决了所有问题，所以本次 `pull request` 不会再有冲突了。

因此通常有两种方式来解决代码冲突：

- `pull request` 的 reviewer 需要解决所有的代码冲突。
- 开发人员需要确保将发布分支的最新代码合并到功能分支，并且解决所有的冲突。


# 还是 Master 分支


一旦项目完成，发布分支的代码需要合并回 master 分支，同时需要发布到生产环境。

因此生产环境中的代码总是和 master 分支保持一致。同时对于今后的任何项目来说都是要确保 master 代码是最新的。




> 我们现在团队就是按照这样的方式进行开发，确实可以尽可能的减少代码管理上的问题。


# 题外话

像之前那篇[《如何成为一位「不那么差」的程序员》](https://crossoverjie.top/2018/08/12/personal/how-to-be-developer/#English-%E6%8C%BA%E9%87%8D%E8%A6%81)说的那样，建议大家都多看看国外的优质博客。

甚至尝试和作者交流，经过沟通原作者也会在原文中贴上我的翻译链接。大家互惠互利使好的文章转播的更广。

![](https://ws3.sinaimg.cn/large/0069RVTdgy1fv0bxa6p94j30uq0rgjvc.jpg)

![](https://ws3.sinaimg.cn/large/0069RVTdgy1fv0bxs6fp9j30us0qydkt.jpg)


**你的点赞与转发是最大的支持。**